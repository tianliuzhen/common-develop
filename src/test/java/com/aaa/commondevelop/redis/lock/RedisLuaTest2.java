package com.aaa.commondevelop.redis.lock;

import com.aaa.commondevelop.config.redis.lock.RedisLuaLock;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * @author liuzhen.tian
 * @version 1.0 main.java  2020/9/14 11:47
 */
@Slf4j
@SpringBootTest
public class RedisLuaTest2 {

    @Autowired
    @Qualifier("redisLuaLockImplV2")
    // @Qualifier("redisLuaLockImpl")
    RedisLuaLock redisLuaLock;

    // 总的请求个数
    public static final int requestTotal = 4;

    // 同一时刻最大的并发线程的个数
    public static final int concurrentThreadNum = 4;


    @Test
    public void mainTest() throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(requestTotal);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(concurrentThreadNum);
        for (int i = 0; i < requestTotal; i++) {
            executorService.execute(() -> {
                try {
                    log.info(Thread.currentThread().getName() + "：到达栅栏");
                    cyclicBarrier.await();
                    log.info(Thread.currentThread().getName() + "：冲破栅栏");
                    lockLuaTest();
                } catch (InterruptedException | BrokenBarrierException e) {
                    log.error("exception", e);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        log.info("请求完成");
    }

    private void tryLockLuaTest() {
        String value = UUID.randomUUID().toString();
        try {
            Boolean aaa = redisLuaLock.tryLock("aaa", value, 10);
            if (!aaa) {
                System.out.println(Thread.currentThread().getName() + "：已经加锁，请等待！");
            } else {
                System.err.println(Thread.currentThread().getName() + "：首先执行的线程，开始执行" + new Date());
                Thread.sleep(2 * 1000);
                System.err.println(Thread.currentThread().getName() + "：首先执行的线程，开始结束" + new Date());
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            redisLuaLock.releaseLock("aaa", value);
        }

    }

    @SneakyThrows
    private void lockLuaTest() {
        String value = UUID.randomUUID().toString();
        try {
            redisLuaLock.lock("lockLuaTest", value, 2);
            System.out.println(Thread.currentThread().getName() + "：获得锁，开始执行！" + ",date:" + new Date());

            Thread.sleep(1000);
        } finally {
            redisLuaLock.releaseLock("lockLuaTest", value);
        }
    }

}
