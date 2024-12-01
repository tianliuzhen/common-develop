package com.aaa.commondevelop.schedule;

/**
 * @author liuzhen.tian
 * @version 1.0 ScheduleTask.java  2024/12/1 22:06
 */

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 定时任务
 *
 * @author wl
 */
@Data
@Slf4j
@Component
@PropertySource("classpath:/task-config.ini")
public class ScheduleTask implements SchedulingConfigurer {

    @Value("${printTime.cron}")
    private String cron;


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // 动态使用cron表达式设置循环间隔
        taskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                log.info("Current time： {}", LocalDateTime.now());
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                // 使用CronTrigger触发器，可动态修改cron表达式来操作循环规则
                CronTrigger cronTrigger = new CronTrigger(cron);
                Date nextExecutionTime = cronTrigger.nextExecutionTime(triggerContext);
                return nextExecutionTime;
            }
        });
    }
}
// 编写一个接口，使得可以通过调用接口动态修改该定时任务的执行时间：
//
//         package com.wl.demo.controller;
//
//         import com.wl.demo.task.ScheduleTask;
//         import lombok.extern.slf4j.Slf4j;
//         import org.springframework.beans.factory.annotation.Autowired;
//         import org.springframework.web.bind.annotation.GetMapping;
//         import org.springframework.web.bind.annotation.RequestMapping;
//         import org.springframework.web.bind.annotation.RestController;
//
// /**
//  * @author wl
//  */
// @Slf4j
// @RestController
// @RequestMapping("/test")
// public class TestController {
//
//     private final ScheduleTask scheduleTask;
//
//     @Autowired
//     public TestController(ScheduleTask scheduleTask) {
//         this.scheduleTask = scheduleTask;
//     }
//
//     @GetMapping("/updateCron")
//     public String updateCron(String cron) {
//         log.info("new cron :{}", cron);
//         scheduleTask.setCron(cron);
//         return "ok";
//     }
