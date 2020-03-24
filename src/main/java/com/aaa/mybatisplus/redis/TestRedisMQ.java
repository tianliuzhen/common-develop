package com.aaa.mybatisplus.redis;

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

    @GetMapping(value = "testRedisSendMessageQ")
    public void sendMessage(){
        redisTemplate.convertAndSend("mq_01", "asdasd");

        redisTemplate.convertAndSend("mq_02", "asdas2");

    }
}
