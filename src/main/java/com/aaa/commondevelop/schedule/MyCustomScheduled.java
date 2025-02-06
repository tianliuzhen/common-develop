package com.aaa.commondevelop.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

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
// @Component
public class MyCustomScheduled {

    /**
     * 允许并发执行（不推荐）
     * 原因：
     *  本身这里的线程作用：只是分配任务，而不是执行任务
     *  无法做出线程数的估计
     *
     * 原理：
     * org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor#finishRegistration()
     *   实例化后置：ScheduledTaskRegistrar 实例化之后会调用此方法
     * org.springframework.scheduling.config.ScheduledTaskRegistrar#setTaskScheduler(org.springframework.scheduling.TaskScheduler)
     *   注入：taskScheduler
     * org.springframework.scheduling.config.ScheduledTaskRegistrar#scheduleTasks()
     *   兜底：如果为设置taskScheduler，采用this.localExecutor = Executors.newSingleThreadScheduledExecutor()
     */
    // @Bean
    // public TaskScheduler taskScheduler() {
    //     ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    //     taskScheduler.setPoolSize(10);
    //     taskScheduler.initialize();
    //     return taskScheduler;
    // }

    // 同上
    // @Configuration
    // public class ScheduleConfig implements SchedulingConfigurer {
    //
    //     @Override
    //     public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    //         ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(10, new BasicThreadFactory.Builder().namingPattern("customized-schedule-pool-%d").daemon(true).build());
    //         taskRegistrar.setScheduler(executorService);
    //     }
    // }

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
