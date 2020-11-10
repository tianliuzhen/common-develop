package com.aaa.mybatisplus.redis.lock;

import com.aaa.mybatisplus.config.redis.lock.RedisLuaLock;
import com.aaa.mybatisplus.config.redis.lockRedisson.RedisLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * @author liuzhen.tian
 * @version 1.0 main.java  2020/9/14 11:47
 */
@Slf4j
@SpringBootTest
public class main {

    @Autowired
    // @Qualifier("redisLuaLockImplV2")
    @Qualifier("redisLuaLockImpl")
    RedisLuaLock redisLuaLock;

    // 总的请求个数
    public static final int requestTotal = 20;

    // 同一时刻最大的并发线程的个数
    public static final int concurrentThreadNum = 20;

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RedisLockUtil redisLockUtil;

    @Test
    public  void mainTest() throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(requestTotal);
        Semaphore semaphore = new Semaphore(concurrentThreadNum);
        for (int i = 0; i< requestTotal; i++) {
            executorService.execute(()->{
                try {
                    semaphore.acquire();
                    // test1
                    // lockLuaTest();
                    // test2
                    lockRedissonTest();
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

    private  void lockLuaTest() {
        String value = UUID.randomUUID().toString();
        try {
            Boolean aaa = redisLuaLock.tryLock("aaa", value, 20);
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
    }

    private void  lockRedissonTest(){
        RLock lock = null;
        try {
            //1. 获取锁
            lock = redissonClient.getLock("zzz");
            // type 1 : 阻塞锁 （不释放锁一直等待），也可设置等待时间，但是既然阻塞了这个时间，也没实际效果，就是分段续约
            // lock.lock(100, TimeUnit.SECONDS);
            // lock.lock();
            // type 2 : 非阻塞锁
            // 非阻塞锁，可以设置锁的执行等待时间
            boolean b = lock.tryLock(0, 10, TimeUnit.SECONDS);
            if (b) {
                System.err.println("首先执行的线程");
            }else {
                System.out.println("已经加锁，请等待！");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // lock.unlock();
        }

    }

}
