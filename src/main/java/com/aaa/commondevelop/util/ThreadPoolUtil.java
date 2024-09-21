package com.aaa.commondevelop.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author liuzhen.tian
 * @version 1.0 ThreadPoolUtil.java  2024/3/19 21:45
 */
public class ThreadPoolUtil {
    /**
     * 重写线程池是为了兼容ThreadLocal参数
     */
    static class MyThreadPoolExecutor extends ThreadPoolExecutor{

        public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }

        public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        }

        public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        }

        public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }

        @Override
        public void execute(Runnable runnable){
            super.execute(wrap(runnable));
        }
        @Override
        public <T> Future<T> submit(Callable<T> task){
            return super.submit(wrap(task));
        }
    }

    public static final ThreadPoolExecutor common_pool =
            new MyThreadPoolExecutor(
                    10,
                    10,
                    60,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(100),
                    new CustomizableThreadFactory("common-pool"));

    public static final ThreadPoolExecutor starmap_pool =
            new MyThreadPoolExecutor(
                    3,
                    6,
                    60,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(10),
                    new ThreadFactoryBuilder().setNameFormat("starMap-pool").build());


    public static Runnable wrap(Runnable runnable) {
        Map<String, String> context = MDC.getCopyOfContextMap();
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            runnable.run();
        };
    }

    public static <T> Callable<T> wrap(Callable<T> runnable) {
        Map<String, String> context = MDC.getCopyOfContextMap();
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            return runnable.call();
        };
    }


    static {
        // 允许超时释放核心线程
        common_pool.allowCoreThreadTimeOut(true);
        starmap_pool.allowCoreThreadTimeOut(true);
    }
}
