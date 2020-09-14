package com.aaa.mybatisplus.redisLockTest;

import com.aaa.mybatisplus.config.redis.lock.RedisLuaLock;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
public class main {

    @Autowired
    RedisLuaLock redisLuaLock;

    // 总的请求个数
    public static final int requestTotal = 100;

    // 同一时刻最大的并发线程的个数
    public static final int concurrentThreadNum = 10;

    @Test
    public  void mainTest() throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(requestTotal);
        Semaphore semaphore = new Semaphore(concurrentThreadNum);
        for (int i = 0; i< requestTotal; i++) {
            executorService.execute(()->{
                try {
                    semaphore.acquire();
                    String result = testRequestUri();
                    log.info("result:{}.", result);
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

    private  String testRequestUri() {
        String value = UUID.randomUUID().toString();
        try {
            Boolean aaa = redisLuaLock.tryLock("aaa", value, "20");
            if (!aaa) {
                System.out.println("已经加锁，请等待！");
            }else {
                System.err.println("首先执行的线程");
            }

        } finally {
            redisLuaLock.releaseLock("aaa", value);
        }

        // Boolean aaa = redisLockLua.releaseLock("aaa", UUID.randomUUID().toString());
        // System.out.println(aaa);
        // redisLockSet.getDistributedLock("aaa", UUID.randomUUID().toString(), 10);
        return "";
    }

}
