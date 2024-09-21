package com.aaa.commondevelop.retry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

/**
 *
 * @author liuzhen.tian
 * @version 1.0 PayService.java  2021/2/19 23:25
 */
@Service
@Slf4j
public class PayService {


    private final int totalNum = 100000;

    /**
     * @Retryable的参数说明：
     *
     * value：抛出指定异常才会重试
     * include：和value一样，默认为空，当exclude也为空时，默认所以异常
     * exclude：指定不处理的异常
     * maxAttempts：最大重试次数，默认3次
     * backoff：重试等待策略，默认使用@Backoff，@Backoff的
     *          value: 初始等待时间，默认为1000L，我们设置为1500L；
     *          maxDelay：最大等待时间，
     *          multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，
     *          如果把multiplier设置为1.5，则第一次重试为2秒，第二次为3秒，第三次为4.5秒。
     */
    @Retryable(value = IllegalArgumentException.class, maxAttempts = 5,
            backoff= @Backoff(value = 1500, maxDelay = 100000, multiplier = 1.2))
    public int minGoodsNum(int num) {
        log.info("减库存开始" + LocalTime.now());
        try {
            int i = 1 / 0;
        } catch (Exception e) {
            log.error("illegal");
        }
        if (num <= 0) {
            throw new IllegalArgumentException("数量不对");
        }
        log.info("减库存执行结束" + LocalTime.now());
        return totalNum - num;
    }

    /**
     * 当重试耗尽时，RetryOperations可以将控制传递给另一个回调，
     * 即RecoveryCallback。Spring-Retry还提供了@Recover注解，
     * 用于@Retryable重试失败后处理方法，此方法里的异常一定要是@Retryable方法里抛出的异常，
     * 否则不会调用这个方法。
     * @param e
     * @return
     */
    @Recover
    public int recover(Exception e) {
        log.warn("减库存失败！！！" + LocalTime.now());
        return totalNum;
    }
}
