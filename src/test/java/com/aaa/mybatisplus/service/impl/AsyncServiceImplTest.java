package com.aaa.mybatisplus.service.impl;

import com.aaa.mybatisplus.service.AsyncService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author liuzhen.tian
 * @version 1.0 AsyncServiceImplTest.java  2022/1/18 21:23
 */

@SpringBootTest
public class AsyncServiceImplTest {

    @Autowired
    AaBaseService aaBaseService;

    @Autowired
    BbBaseService bbBaseService;

    @Autowired
    AsyncService asyncService;

    @Test
    public void test() {
        aaBaseService.queryData();
    }

    @Test
    public void test2() {
        bbBaseService.queryData();
    }
}
