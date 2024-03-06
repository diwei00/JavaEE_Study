package com.xuecheng.media.service.jobhandler;

import com.xuecheng.base.utils.Mp4VideoUtil;
import com.xuecheng.media.model.po.MediaProcess;
import com.xuecheng.media.service.MediaFileProcessService;
import com.xuecheng.media.service.MediaFileService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 视频处理类
 */
@Slf4j
@Component
public class VideoTask {

    @Autowired
    private MediaFileService mediaFileService;
    @Autowired
    private MediaFileProcessService mediaFileProcessService;
    @Value("${videoprocess.ffmpegpath}")
    private String ffmpegPath;


    /**
     * 视频处理任务
     *
     * @throws Exception
     */
    @XxlJob("videoJobHandler")
    public void videoJobHandler() throws Exception {
        // 分片参数（调度器给的）
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();
        List<MediaProcess> mediaProcessList = null;
        int size = 0;
        try {
            // 取出 cpu 核心数作为一次处理数据的条数
            int processors = Runtime.getRuntime().availableProcessors();
            // 一次处理视频数量不要超过 cpu 核心数
            mediaProcessList = mediaFileProcessService.getMediaProcessList(shardIndex, shardTotal, processors);
            size = mediaProcessList.size();
            log.debug("取出待处理视频任务{}条", size);
            if (size < 0) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        // 启动 size 个线程的线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(size);
        // 计数器，保证线程任务处理完成，该方法结束（保证任务调度的合理性）
        CountDownLatch countDownLatch = new CountDownLatch(size);
        // 将处理任务加入线程池
        mediaProcessList.forEach(mediaProcess -> {
            threadPool.execute(() -> {
                try {
                    // 任务 id
                    Long taskId = mediaProcess.getId();
                    // 抢占任务（使用分布式锁，保证分布式系统下任务不被重复消费）
                    boolean b = mediaFileProcessService.startTask(taskId);
                    if (!b) {
                        return;
                    }
                    log.debug("开始执行任务:{}", mediaProcess);
                    // 下边是处理逻辑
                    // 桶
                    String bucket = mediaProcess.getBucket();
                    // 存储路径
                    String filePath = mediaProcess.getFilePath();
                    // 原始视频的 md5 值
                    String fileId = mediaProcess.getFileId();
                    // 原始文件名称
                    String filename = mediaProcess.getFilename();
                    // 将要处理的文件下载到服务器上
                    File originalFile = mediaFileService.downloadFileFromMinIO(mediaProcess.getBucket(),
                            mediaProcess.getFilePath());
                    if (originalFile == null) {
                        log.debug("下载待处理文件失败,originalFile:{}",
                                mediaProcess.getBucket().concat(mediaProcess.getFilePath()));
                        // 保存处理失败原因
                        mediaFileProcessService.saveProcessFinishStatus(mediaProcess.getId()
                                , "3", fileId, null, "下载待处理文件失败");
                        return;
                    }
                    // 处理下载的视频文件
                    File mp4File = null;
                    try {
                        mp4File = File.createTempFile("mp4", ".mp4");
                    } catch (IOException e) {
                        log.error("创建 mp4 临时文件失败");
                        // 保存处理失败原因
                        mediaFileProcessService.saveProcessFinishStatus(mediaProcess.getId()
                                , "3", fileId, null, "创建 mp4 临时文件失败");
                        return;
                    }
                    // 视频处理结果
                    String result = "";
                    try {
                        // 开始处理视频
                        Mp4VideoUtil videoUtil = new Mp4VideoUtil(ffmpegPath, originalFile.getAbsolutePath(),
                                mp4File.getName(), mp4File.getAbsolutePath());
                        // 开始视频转换，成功将返回 success
                        result = videoUtil.generateMp4();
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("处理视频文件:{},出错:{}", mediaProcess.getFilePath(), e.getMessage());
                    }
                    if (!result.equals("success")) {
                        // 记录错误信息
                        log.error("处理视频失败,视频地址:{},错误信息:{}", bucket + filePath, result);
                        mediaFileProcessService.saveProcessFinishStatus(mediaProcess.getId()
                                , "3", fileId, null, result);
                        return;
                    }
                    // 将 mp4 上传至 minio
                    // mp4 在 minio 的存储路径
                    String objectName = getFilePath(fileId, ".mp4");
                    // 访问 url
                    String url = "/" + bucket + "/" + objectName;
                    try {
                        mediaFileService.addMediaFilesToMinIO(mp4File.getAbsolutePath(),
                                "video/mp4", bucket, objectName);
                        // 将 url 存储至数据，并更新状态为成功，并将待处理视频记录删除存入历史
                        mediaFileProcessService.saveProcessFinishStatus(mediaProcess.getId()
                                , "2", fileId, url, null);
                    } catch (Exception e) {
                        log.error("上传视频失败或入库失败,视频地址:{},错误 信息:{}", bucket + objectName, e.getMessage());
                        // 记录错误信息
                        mediaFileProcessService.saveProcessFinishStatus(mediaProcess.getId()
                                , "3", fileId, null, "处理后视频上传或入库失败");
                    }
                } finally {
                    // 保证一定会执行，防止上面return，该方法一直阻塞
                    // 当size为0时方法结束
                    countDownLatch.countDown();
                }
            });
        });
        // 等待,给一个充裕的超时时间,防止无限等待，到达超时时间还没有处理完成则结束任务
        countDownLatch.await(30, TimeUnit.MINUTES);
    }

    private String getFilePath(String fileMd5, String fileExt) {
        return fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/" + fileMd5 + fileExt;
    }

}
