package com.aaa.mybatisplus.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author liuzhen.tian
 * @version 1.0 ThreadPoolUtil.java  2024/3/19 21:45
 */
public class ThreadPoolUtil {
    public static final ThreadPoolExecutor common_pool =
            new ThreadPoolExecutor(
                    7,
                    10,
                    60,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(100),
                    new CustomizableThreadFactory("common_pool"));

    public static final ThreadPoolExecutor starmap_pool =
            new ThreadPoolExecutor(
                    3,
                    6,
                    60,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(10),
                    new ThreadFactoryBuilder().setNameFormat("starmap_pool").build());

    static {
        // 允许超时释放核心线程
        common_pool.allowCoreThreadTimeOut(true);
        starmap_pool.allowCoreThreadTimeOut(true);
    }
}
