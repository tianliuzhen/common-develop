package com.aaa.mybatisplus.redis.demo;

import com.aaa.mybatisplus.config.redis.RedisDbMany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * @author liuzhen.tian
 * @version 1.0 TestRedisChangeDb.java  2020/10/14 11:11
 */
@RestController
public class TestRedisChangeDb {
    private ValueOperations<String, String> valueOperations;
    @Autowired
    private RedisDbMany redisDbMany;


    @PostConstruct
    public void init() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisDbMany.getLettuceConnectionFactoryByIndex(5));
        redisTemplate.afterPropertiesSet();
        valueOperations = redisTemplate.opsForValue();
    }

    @GetMapping(value = "/testRedisChangeDb")
    public void setValueOperations(){
        valueOperations.set("test:db5","123");
    }
}
