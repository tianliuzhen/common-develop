package com.aaa.mybatisplus.redis.delay;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author liuzhen.tian
 * @version 1.0 RedisDelayTest.java  2020/11/10 17:46
 */
@SpringBootTest
public class RedisDelayTest  {



    @Autowired
    RedisTemplate redisTemplate;
    String QUEUENAME = "zzh:queue";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Test
    public void pr() {
        System.out.println(  redisTemplate.opsForZSet().score(QUEUENAME, 1605003343788L));
    }




}
