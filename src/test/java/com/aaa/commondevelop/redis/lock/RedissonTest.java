package com.aaa.commondevelop.redis.lock;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.concurrent.*;

/**
 * @author liuzhen.tian
 * @version 1.0 main.java  2020/9/14 11:47
 */
@Slf4j
@SpringBootTest
public class RedissonTest {

    // 总的请求个数
    public static final int requestTotal = 20;

    // 同一时刻最大的并发线程的个数
    public static final int concurrentThreadNum = 20;

    @Qualifier("redissonClient")
    @Autowired
    private RedissonClient redissonClient;

    @Test
    public  void mainTest() throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(requestTotal);
        Semaphore semaphore = new Semaphore(concurrentThreadNum);
        for (int i = 0; i< requestTotal; i++) {
            executorService.execute(()->{
                try {
                    semaphore.acquire();
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
            // 参数1：意思是最大等待时间， 参数2：
            // 尝试加锁，最多等待3秒，上锁以后3秒自动解锁
            boolean b = lock.tryLock(3,3, TimeUnit.SECONDS);
            if (b) {
                System.err.println(Thread.currentThread().getId()+"true: " + new Date());
                Thread.sleep(1000*5);
                System.err.println("首先执行的线程");
                try {
                    //处理
                    log.info("tryLock thread---{}, lock:{}", Thread.currentThread().getId(), lock);
                } catch (Exception e) {
                } finally {
                    //解锁
                    lock.unlock();
                }
                System.err.println(Thread.currentThread().getId()+"true: " + new Date());

            }else {
                System.out.println("false: " + new Date());
                System.out.println("已经加锁，请等待！");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //处理
            //保留中断发生的证据，以便调用栈中更高层的代码能知道中断，并对中断作出响应
            Thread.currentThread().interrupt();
        }

    }

    @Test
    public void test(){
        RLock lock = null;
        try {
            //1. 获取锁
            lock = redissonClient.getLock("zzz:lock");
            lock.lock();
        }catch (Exception e){

        }
    }
}
