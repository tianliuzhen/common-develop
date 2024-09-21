package com.aaa.commondevelop.redis.stock;

import com.aaa.commondevelop.config.redis.stock.SubtractStockByLua;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author liuzhen.tian
 * @version 1.0 SeckillSubtractStockTest.java  2021/11/18 22:47
 */
@Slf4j
@SpringBootTest
public class SeckillSubtractStockTest {

    public static final String IPHONE_13 = "iphone13";

    @Autowired
    SubtractStockByLua subtractStockByLua;

    // 总的请求个数
    public static final int requestTotal = 20;

    // 同一时刻最大的并发线程的个数
    public static final int concurrentThreadNum = 5;

    @Autowired
    private RedisTemplate redisTemplate;


    @Test
    public void mainTest() throws InterruptedException {
        // TODO: mock从db中 初始化redis数据
        // 初始化 iphone13 库存为 10
        redisTemplate.opsForValue().set(IPHONE_13, 100);

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch countDownLatch = new CountDownLatch(requestTotal);
        Semaphore semaphore = new Semaphore(concurrentThreadNum);
        for (int i = 0; i < requestTotal; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();

                    // 执行抢购手机
                    subStock(IPHONE_13, 1);

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
     *
     * @param key 库存key
     * @param num 购买件数
     */
    private void subStock(String key, Integer num) {

        /**
         * 场景：
         * 1：某个秒杀环境，需要要求每个用户总共购买 x 件。
         * 2：某个秒杀环境，需要要求每个用户单次购买 x 件。
         *
         * @param goodsStockKey 商品库存key
         * @param num           购买件数
         * @param userId        用户id
         * @param limitNum      限制购买件数
         */
        boolean res = subtractStockByLua.subtractStockLimitNum(
                key,
                num,
                "userId:" + Thread.currentThread().getId(),
                3);
        if (res) {
            // todo： 抢到
            System.out.println(res);
        } else {
            // todo：未抢到
            System.err.println(res);
        }
    }

}
