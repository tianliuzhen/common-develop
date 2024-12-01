package com.aaa.commondevelop.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 默认使用 @Scheduled 注解的任务，都是单线程执行
 * 可以有俩个方案实现多线程执行：
 * 1. @Scheduled 任务里面使用 异步处理
 * 2. 配置TaskScheduler 为多核心任务
 *
 * @author liuzhen.tian
 * @version 1.0 MyScheduled.java  2024/7/14 20:38
 */
@Slf4j
@Component
public class MyCustomScheduled {

    /**
     * 允许并发执行
     */
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        taskScheduler.initialize();
        return taskScheduler;
    }

    @Scheduled(cron = "0/11 * * * * ?")
    public void run1() {
        // ThreadPoolUtil.common_pool.execute(() -> {
        try {
            log.error("run1.begin:" + new Date());
            Thread.sleep(5000);
            log.error("run1.end:" + new Date());
        } catch (InterruptedException ignored) {
        }

        // });

    }

    @Scheduled(cron = "0/11 * * * * ?")
    public void run2() {
        // ThreadPoolUtil.common_pool.execute(() -> {
        try {

            log.error("run2.begin:" + new Date());
            Thread.sleep(5000);
            log.error("run2.end:" + new Date());
        } catch (InterruptedException e) {
        }
        // });
    }
}
