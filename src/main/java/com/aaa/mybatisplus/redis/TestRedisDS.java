package com.aaa.mybatisplus.redis;

import com.aaa.mybatisplus.domain.entity.User;
import com.aaa.mybatisplus.domain.enums.GenderEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/3/25
 */
@RestController
@Slf4j
public class TestRedisDS {
    @Autowired
    RedisTemplate redisTemplate1;

    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping(value = "/testManyRedis")
    public void testManyRedis(){
        redisTemplate1.opsForValue().set("qwe","31");
        redisTemplate.opsForValue().set("qwe","31");
    }

    @GetMapping("/my-redis-cache1")
    @ResponseBody
    @Cacheable(value="my-redis-cache1",cacheManager = "cacheManager",keyGenerator="allParamGenerator")
    public Map test(){
        Map map=new HashMap();
        map.put(2,"d");
        map.put(1,"d");
        map.put(3,"d");
        Map map2=new HashMap();
        map2.put(2,"d");
        map2.put(1,"d");
        map2.put(3,map);
        log.info("测试是否执行");
        return map2;
    }
    @GetMapping("/my-redis-cache2")
    @ResponseBody
    @Cacheable(value="my-redis-cache2",cacheManager = "cacheManager",keyGenerator="allParamGenerator")
    public Object test2(){
        List<User> list = new ArrayList<>();
        User u = new User().setAge(GenderEnum.FEMALE).setId("1");
        User u2 = new User().setAge(GenderEnum.MALE).setId("2");
        list.add(u);
        list.add(u2);
        log.info("测试是否执行");
        return list;
    }
}
