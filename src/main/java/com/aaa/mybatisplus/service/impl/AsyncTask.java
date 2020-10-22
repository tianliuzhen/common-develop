package com.aaa.mybatisplus.service.impl;

import com.aaa.mybatisplus.domain.entity.Entity;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;


/**
 * @author liuzhen.tian
 * @version 1.0 AsyncTask.java  2020/10/20 23:25
 */
@Component
public class AsyncTask {

    /**
     * 异步调用第三方接口查询
     */
    @Async
    public void queryTask(Entity entity, CountDownLatch countDownLatch){
        // 这里模拟测试接口
        String response = doPost(entity.getRequest());
        entity.setResponse(response);
        countDownLatch.countDown();

    }

    @SneakyThrows
    private String doPost(String request) {
        Thread.sleep(1500);
        return request+"?response=123";
    }


}
