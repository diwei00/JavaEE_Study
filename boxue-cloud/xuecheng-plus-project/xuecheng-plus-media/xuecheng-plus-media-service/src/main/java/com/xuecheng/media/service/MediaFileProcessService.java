package com.xuecheng.media.service;

import com.xuecheng.media.model.po.MediaProcess;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MediaFileProcessService {
    /**
     * @param shardIndex 分片序号
     * @param shardTotal 分片总数
     * @param count      获取记录数
     * @return java.util.List<com.xuecheng.media.model.po.MediaProcess>
     * @description 获取待处理任务
     */
    List<MediaProcess> getMediaProcessList(int shardIndex, int shardTotal, int count);

    Boolean startTask( long id);

    /**
     * @description 保存任务结果
     * @param taskId 任务 id
     * @param status 任务状态
     * @param fileId 文件 id
     * @param url url
     * @param errorMsg 错误信息
     * @return void
     */
    void saveProcessFinishStatus(Long taskId,String status,String fileId,String url,String errorMsg);
}