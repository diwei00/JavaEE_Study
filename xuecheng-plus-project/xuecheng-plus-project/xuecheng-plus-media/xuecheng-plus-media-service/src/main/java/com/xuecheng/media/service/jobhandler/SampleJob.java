package com.xuecheng.media.service.jobhandler;


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
}
