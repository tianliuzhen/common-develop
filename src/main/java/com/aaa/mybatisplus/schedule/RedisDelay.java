package com.aaa.mybatisplus.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Set;

/**
 * @author liuzhen.tian
 * @version 1.0 RedisDeplay.java  2020/11/10 18:00
 */
@Component
public class RedisDelay {

    @Autowired
    private RedisTemplate redisTemplate;
    private String QUEUENAME = "zzh:queue";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * @param
     * @method 每间隔1秒执行一次
     */
    // @Scheduled(cron = "*/1 * * * * * ")
    public void cs() {
        // System.out.println("------------等待消费--------------");
        //取出QUEUENAME集合中的score在0-当前时间戳这个范围的所有值
        Set<Integer> set = redisTemplate.opsForZSet().rangeByScore(QUEUENAME, 0, System.currentTimeMillis());
        Iterator<Integer> iterator = set.iterator();
        while (iterator.hasNext()) {
            Integer value = iterator.next();
            //遍历取出每一个score
            Double score = redisTemplate.opsForZSet().score(QUEUENAME, value);
            //达到了时间就进行消费
            if (System.currentTimeMillis() > score) {
                System.out.println("消费了：" + value + "消费时间为：" + simpleDateFormat.format(System.currentTimeMillis()));
                redisTemplate.opsForZSet().remove(QUEUENAME, value);

            }
        }
    }
}
