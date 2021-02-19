package com.aaa.mybatisplus.retry;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author liuzhen.tian
 * @version 1.0 RetryTest.java  2021/2/19 23:30
 */
@SpringBootTest
public class RetryTest {
    @Autowired
    private PayService payService;

    @Test
    public void payTest() throws Exception {
        int store = payService.minGoodsNum(-1);
        System.out.println("库存为：" + store);
    }
}
