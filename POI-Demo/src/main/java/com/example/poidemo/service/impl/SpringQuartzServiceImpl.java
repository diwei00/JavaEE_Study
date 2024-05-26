package com.example.poidemo.service.impl;

import cn.hutool.core.util.StrUtil;
import com.example.poidemo.service.SpringQuartzService;
import com.example.poidemo.task.ExecuteTask;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * @Description
 * @Author wh
 * @Date 2024/5/26 16:29
 */

@Slf4j
@Service
public class SpringQuartzServiceImpl implements SpringQuartzService {
   @Override
   public Boolean test(String cron) {
      if(StrUtil.isBlank(cron)) {
         log.info("cron表达式为空, 定时任务添加失败！");
         return false;
      }
      try {
         // 可动态指定调度器名称，不在配置文件中写
         Properties properties = new Properties();
         // 调度器名称（可在Quartz上下文对象中根据调度器名称获取调度器）
         properties.put("org.quartz.scheduler.instanceName", "scheduler");
         // 线程池数量，必须指定
         properties.put("org.quartz.threadPool.threadCount", "5");
         StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory(properties);

         // 创建调度器
         Scheduler scheduler = stdSchedulerFactory.getScheduler();
         // 定义JobDetail对象，指定需要执行的任务
         JobDetail jobDetail = JobBuilder.newJob(ExecuteTask.class)
                 .withIdentity("job1", "group1")
                 .storeDurably() // 即使没有触发器绑定当前JobDetail对象,也不会被删除
                 .build();
         // 创建触发器
         CronTrigger trigger = TriggerBuilder.newTrigger()
                 .withIdentity("trigger1", "group1")
                 .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                 .build();
         scheduler.scheduleJob(jobDetail, trigger);

         // 启动调度器
         scheduler.start();

      } catch (SchedulerException e) {
         log.info("创建调度器失败");
         return false;
      }


      return true;
   }
}
