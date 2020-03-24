package com.aaa.mybatisplus.redis.mq2;

import com.aaa.mybatisplus.redis.mq.RedisMessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description: 实现redis mq
 *              其实 用 lpush 和 lpop 一样可以实现的
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/3/24
 */
@RestController
public class TestRedisMQ {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    RedisMessagePublisher redisMessagePublisher;

    @GetMapping(value = "testRedisSendMessageQ")
    public void sendMessage(){
        // 实现方法一
        redisTemplate.convertAndSend("mq_01", "test");

        redisTemplate.convertAndSend("mq_02", "test2");

        // 实现方法二
        redisMessagePublisher.publish("test3");
    }
}
