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
        for (int i = 1; i <= 1; i++) {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());
            now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + i);
            System.out.println("生产了：" + i + "当前时间为：" + simpleDateFormat.format(System.currentTimeMillis()) + "消费时间为：" + simpleDateFormat.format(now.getTime()));
            //往QUEUENAME这个集合中放入i，设置scorce为排序规则
            redisTemplate.opsForZSet().add(QUEUENAME, i, now.getTime().getTime());
        }

    }



}
