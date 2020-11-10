package com.aaa.mybatisplus.redis.delay;

import com.aaa.mybatisplus.util.RedisDelayingQueue;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * @author liuzhen.tian
 * @version 1.0 RedisDelayTest.java  2020/11/10 17:46
 */
@SpringBootTest
public class RedisDelayTest  {
    @Autowired
    private RedisDelayingQueue<String> redisDelayingQueue;

    @Test
    public void  test(){
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                redisDelayingQueue.delay("codehole" + i);
            }
        });

        Thread consumer = new Thread(() -> {
            redisDelayingQueue.loop();
        });
        producer.start();
        consumer.start();
        try {
            producer.join();
            Thread.sleep(6000);
            consumer.interrupt();
            consumer.join();
        } catch (InterruptedException e) {
        }
    }


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
