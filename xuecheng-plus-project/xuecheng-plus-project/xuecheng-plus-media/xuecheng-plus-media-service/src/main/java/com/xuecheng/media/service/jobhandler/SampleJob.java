package com.xuecheng.media.service.jobhandler;


import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * xxl-job任务类
 */
@Component
@Slf4j
public class SampleJob {

    /**
     * 任务（Bean 模式）
     * xxlJob下发任务后，执行器执行任务
     */
    @XxlJob("testJob")
    public void testJob() throws Exception {
        log.info("开始执行.....");
    }

    /**
     * 2、分片广播任务
     * 执行器可获取xxl-job分配的 分片序号 和 总分片数
     */
    @XxlJob("shardingJobHandler")
    public void shardingJobHandler() throws Exception {

        // 分片参数
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();
        log.info("分片参数：当前分片序号 = {}, 总分片数 = {}", shardIndex, shardTotal);
        log.info("开始执行第" + shardIndex + "批任务");
    }
}
