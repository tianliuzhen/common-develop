package com.aaa.mybatisplus.redis;

import com.aaa.mybatisplus.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
public class TestRedisString {

    @Autowired
    RedisTemplate redisTemplate;


    @PostMapping("/testRedisStr")
    public void testRedisStr(){
        redisTemplate.opsForValue().set("a","111");
        redisTemplate.opsForValue().set("b","222");
        redisTemplate.opsForValue().set("c","333");
        //1. 同时获取多个key
        List list = redisTemplate.opsForValue().multiGet(new ArrayList(Arrays.asList("a", "b")));
        list.forEach(System.out::println);
        //2. 设定key的值，并返回key的旧值。
        Object c = redisTemplate.opsForValue().getAndSet("c", 123);
        System.out.println(c);
        //3.  INCR命令key中存储的值+1,如果不存在key，则key中的值话先被初始化为0再加1
        Long c1 = redisTemplate.opsForValue().increment("c", 10);
        System.out.println(c1);
        //3.  key中的值自减一,同上
        Long c2 = redisTemplate.opsForValue().decrement("c", 9);
        System.out.println(c2);
    }
}
