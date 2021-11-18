package com.aaa.mybatisplus.redis.stock;

import com.aaa.mybatisplus.config.redis.SubtractStockByLua;
import com.aaa.mybatisplus.config.redis.lock.RedisLuaLock;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author liuzhen.tian
 * @version 1.0 RedisLuaSubtractStockTest.java  2021/11/18 22:47
 */
@Slf4j
@SpringBootTest
public class RedisLuaSubtractStockTest {

    public static final String IPHONE_13 = "iphone13";
    @Autowired
    SubtractStockByLua subtractStockByLua;

    // 总的请求个数
    public static final int requestTotal = 20;

    // 同一时刻最大的并发线程的个数
    public static final int concurrentThreadNum = 20;

    @Autowired
    private RedisTemplate redisTemplate;

    @Before
    public void initData() {

    }

    @Test
    public void mainTest() throws InterruptedException {
        // TODO: mock从db中 初始化redis数据
        // 初始化 iphone13 库存为 10
        redisTemplate.opsForValue().set(IPHONE_13, 10);

        ExecutorService executorService = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(requestTotal);
        Semaphore semaphore = new Semaphore(concurrentThreadNum);
        for (int i = 0; i < requestTotal; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();

                    // 执行抢购手机
                    subStock();

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

    /**
     * 执行减库存
     */
    private void subStock() {
        boolean res = subtractStockByLua.subtractStock(IPHONE_13);
        if (res) {
            // todo： 抢到
            System.out.println(res);
        } else {
            // todo：未抢到
            System.err.println(res);
        }
    }

}
