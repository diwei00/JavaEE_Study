package com.example.poidemo.task;

import com.example.poidemo.service.UserService;
import com.example.poidemo.service.impl.UserServiceImpl;
import com.example.poidemo.util.SpringBeanUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @Description 定时任务具体执行的任务
 * @Author wh
 * @Date 2024/5/26 16:48
 */
public class ExecuteTask implements Job {

    @Override
    public void execute(JobExecutionContext executionContext) throws JobExecutionException {
        // executionContext调度上下文对象，可以获取某些参数
        System.out.println("执行定时任务");
        // 这里需要从Spring容器中取对象，由于调用方指定class字节码，实际就是new对象进行使用
        UserService userService = SpringBeanUtil.getBean(UserServiceImpl.class);
        userService.test();
    }
}
