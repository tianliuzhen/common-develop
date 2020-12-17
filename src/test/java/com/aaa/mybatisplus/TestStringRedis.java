package com.aaa.mybatisplus;

import com.aaa.mybatisplus.domain.entity.City;
import com.aaa.mybatisplus.domain.entity.People;
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
 * 在单个泛型代码上添加@SuppressWarnings("rawtypes")
 * 可以跳过泛型检查，但是需要注意： 还需要在方法上添加 @SuppressWarnings("unchecked")注释
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/3/22
 */
@SpringBootTest
@SuppressWarnings("unchecked")
public class TestStringRedis {

    @Autowired
    @SuppressWarnings("rawtypes")
    RedisTemplate redisTemplate;


    @Test
    @SuppressWarnings("rawtypes")
    public void testString() {

        // 测试set
        redisTemplate.opsForValue().set("a","b");
        redisTemplate.opsForValue().set("b","c");
        List list = redisTemplate.opsForValue().multiGet(new ArrayList(Arrays.asList("a", "b")));
        list.forEach(System.out::println);

        People people = new People("tom", 11, new City(), true, new ArrayList());
        redisTemplate.opsForValue().set("tom",people);
        People tom = (People)redisTemplate.opsForValue().get("tom");
        System.out.println(tom);


        // 测试 hash
        List map = Arrays.asList(new People("tom", 11, new City(), true, new ArrayList()),
                new People("tom2", 12, new City(), true, new ArrayList())
        );
        redisTemplate.opsForHash().put("map:key","t1",map);
        List peoples= (List) redisTemplate.opsForHash().get("map:key","t1");
        System.out.println(peoples);
    }


}
