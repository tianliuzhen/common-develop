package com.aaa.mybatisplus.util;

/**
 * @author liuzhen.tian
 * @version 1.0 RedisDelayingQueue.java  2020/11/10 17:02
 */
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.UUID;


@Component
public class RedisDelayingQueue<T> {

    private ZSetOperations<String, String> zSet;

    RedisDelayingQueue(RedisTemplate<String,String> redisTemplate) {
        zSet = redisTemplate.opsForZSet();
    }

    static class TaskItem<T> {
        public String id;
        public T msg;
    }

    // fastjson 序列化对象中存在 generic 类型时，需要使用 TypeReference
    private Type TaskType = new TypeReference<TaskItem<T>>() {}.getType();

    private String queueKey;

    public void delay(T msg) {
        TaskItem<T> task = new TaskItem<T>();
        task.id = UUID.randomUUID().toString(); // 分配唯一的 uuid
        task.msg = msg;
        String s = JSON.toJSONString(task); // fastjson 序列化
        zSet.add(queueKey, s, System.currentTimeMillis() + 5000);// 塞入延时队列 ,5s 后再试
    }

    public void loop() {
        while (!Thread.interrupted()) {
            // 只取一条

            Set<String> values =  zSet.rangeByScore(queueKey, 0,   System.currentTimeMillis(),0,1);
            if (values.isEmpty()) {
                try {
                    Thread.sleep(500); // 歇会继续
                } catch (InterruptedException e) {
                    break;
                }
                continue;
            }
            String s = values.iterator().next();
            if (zSet.score(queueKey, s) > 0) { // 抢到了
                TaskItem<T> task = JSON.parseObject(s, TaskType); // fastjson 反序列化
                this.handleMsg(task.msg);
            }
        }
    }

    public void handleMsg(T msg) {
        System.out.println(msg);
    }

}
