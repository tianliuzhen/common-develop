package com.aaa.commondevelop.redis.lock;

import com.aaa.commondevelop.config.redis.lock.RedisLuaLock;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author liuzhen.tian
 * @version 1.0 main.java  2020/9/14 11:47
 */
@Slf4j
@SpringBootTest
public class RedisLuaTest {

    @Autowired
    // @Qualifier("redisLuaLockImplV2")
    @Qualifier("redisLuaLockImpl")
    RedisLuaLock redisLuaLock;

    // 总的请求个数
    public static final int requestTotal = 20;

    // 同一时刻最大的并发线程的个数
    public static final int concurrentThreadNum = 20;


    @Test
    public void mainTest() throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(requestTotal);
        Semaphore semaphore = new Semaphore(concurrentThreadNum);
        for (int i = 0; i < requestTotal; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    // test1
                    lockLuaTest();
                    // test2
                    // lockRedissonTest();
                    semaphore.release();
                } catch (InterruptedException e) {
                    log.error("exception", e);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        log.info("请求完成");
    }

    private void lockLuaTest() {
        String value = UUID.randomUUID().toString();
        try {
            Boolean aaa = redisLuaLock.tryLock("aaa", value, 10);
            if (!aaa) {
                System.out.println(Thread.currentThread().getName() + "：已经加锁，请等待！");
            } else {
                Thread.sleep(6 * 1000);
                System.err.println(Thread.currentThread().getName() + "：首先执行的线程");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            redisLuaLock.releaseLock("aaa", value);
        }

        // Boolean aaa = redisLockLua.releaseLock("aaa", UUID.randomUUID().toString());
        // System.out.println(aaa);
        // redisLockSet.getDistributedLock("aaa", UUID.randomUUID().toString(), 10);
    }

}
