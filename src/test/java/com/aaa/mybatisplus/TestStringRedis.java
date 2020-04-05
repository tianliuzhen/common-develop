package com.aaa.mybatisplus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/3/22
 */
@SpringBootTest
public class TestStringRedis {

    @Autowired
    RedisTemplate redisTemplate;


    @Test
    public void testString() {
        redisTemplate.opsForValue().set("a","b");
        redisTemplate.opsForValue().set("b","c");
        List list = redisTemplate.opsForValue().multiGet(new ArrayList(Arrays.asList("a", "b")));
        list.forEach(System.out::println);
    }


}
