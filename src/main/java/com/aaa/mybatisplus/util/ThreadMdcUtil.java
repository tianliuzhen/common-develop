package com.aaa.mybatisplus.util;


import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author liuzhen.tian
 * @version 1.0 ThreadMdcUtil.java  2023/4/25 21:49
 */
public class ThreadMdcUtil {
    private static ThreadPoolExecutor pool = new ThreadPoolExecutor(
            2,
            4,
            5,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(5));

    public void run(Runnable runnable) {
        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        pool.execute(wrap(runnable, copyOfContextMap));
    }

    public static Runnable wrap(Runnable runnable, Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            runnable.run();
        };
    }

    public static <T> Callable<T> wrap(Callable<T> runnable, Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            return runnable.call();
        };
    }

}
