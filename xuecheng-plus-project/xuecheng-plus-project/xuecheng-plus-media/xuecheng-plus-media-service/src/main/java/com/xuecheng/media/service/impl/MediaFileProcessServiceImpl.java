package com.xuecheng.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.media.mapper.MediaFilesMapper;
import com.xuecheng.media.mapper.MediaProcessHistoryMapper;
import com.xuecheng.media.mapper.MediaProcessMapper;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.model.po.MediaProcess;
import com.xuecheng.media.model.po.MediaProcessHistory;
import com.xuecheng.media.service.MediaFileProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
public class MediaFileProcessServiceImpl implements MediaFileProcessService {

    @Autowired
    private MediaProcessMapper mediaProcessMapper;

    @Autowired
    private MediaFilesMapper mediaFilesMapper;

    @Autowired
    private MediaProcessHistoryMapper mediaProcessHistoryMapper;


    /**
     * 获取未处理任务
     * @param shardIndex 分片序号
     * @param shardTotal 分片总数
     * @param count      获取记录数
     * @return
     */
    @Override
    public List<MediaProcess> getMediaProcessList(int shardIndex, int shardTotal, int count) {
        return mediaProcessMapper.selectListByShardIndex(shardTotal, shardIndex, count);
    }

    /**
     * 分布式锁开启任务
     * @param id 任务id
     * @return true: 成功  false: 失败
     */
    @Override
    public Boolean startTask(long id) {
        int result = mediaProcessMapper.startTask(id);
        return result > 0;
    }

    /**
     * 视频处理成功，更新数据库状态
     * @param taskId 任务 id
     * @param status 任务状态
     * @param fileId 文件 id
     * @param url url
     * @param errorMsg 错误信息
     */
    @Transactional
    @Override
    public void saveProcessFinishStatus(Long taskId, String status, String fileId, String url, String errorMsg) {

        // 查询任务是否存在
        MediaProcess mediaProcess = mediaProcessMapper.selectById(taskId);
        if (mediaProcess == null) {
            return;
        }
        // 处理失败，更新任务处理结果
        LambdaQueryWrapper<MediaProcess> queryWrapperById = new
                LambdaQueryWrapper<MediaProcess>().eq(MediaProcess::getId, taskId);
        // 处理失败
        if (status.equals("3")) {
            MediaProcess mediaProcess_u = new MediaProcess();
            mediaProcess_u.setStatus("3");
            mediaProcess_u.setErrormsg(errorMsg);
            mediaProcess_u.setFailCount(mediaProcess.getFailCount() + 1);
            mediaProcessMapper.update(mediaProcess_u, queryWrapperById);
            log.debug("更新任务处理状态为失败，任务信息:{}",mediaProcess_u);
            return;
        }
        //任务处理成功
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileId);
        if (mediaFiles != null) {
        //更新媒资文件中的访问 url
            mediaFiles.setUrl(url);
            mediaFilesMapper.updateById(mediaFiles);
        }
        //处理成功，更新 url 和状态
        mediaProcess.setUrl(url);
        mediaProcess.setStatus("2");
        mediaProcess.setFinishDate(LocalDateTime.now());
        mediaProcessMapper.updateById(mediaProcess);
        //添加到历史记录
        MediaProcessHistory mediaProcessHistory = new MediaProcessHistory();
        BeanUtils.copyProperties(mediaProcess, mediaProcessHistory);
        mediaProcessHistoryMapper.insert(mediaProcessHistory);
        //删除 mediaProcess
        mediaProcessMapper.deleteById(mediaProcess.getId());
    }

}
