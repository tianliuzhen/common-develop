package com.aaa.mybatisplus.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

/**
 * description: ZSet
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/3/22
 */
@RestController
public class testRedisZSet {
    @Autowired
    RedisTemplate redisTemplate;

    @PostMapping("/testRedisZSet")
    public void testRedisStr(){
        //1. ZADD key score1 memeber1
        DefaultTypedTuple a = new DefaultTypedTuple("b", 2.0);
        DefaultTypedTuple c = new DefaultTypedTuple("c", 3.0);
        Set<DefaultTypedTuple> set = new HashSet<>();
        set.add(a); set.add(c);
        redisTemplate.opsForZSet().add("zs:a","a",1);
        redisTemplate.opsForZSet().add("zs:a",set);
        //2. ZCARD key ：获取集合中的元素数量
        redisTemplate.opsForZSet().zCard("zs:a");
        //3.  ZCOUNT key min max 计算在有序集合中指定区间分数的成员数
        redisTemplate.opsForZSet().rangeByScore("zs:a", 1, 2);
        //4. ZCOUNT key min max 计算在有序集合中指定范围的成员数
        redisTemplate.opsForZSet().rangeByLex("zs:a", new RedisZSetCommands.Range());
        //5. ZRANK key member：返回有序集合指定成员的索引
        redisTemplate.opsForZSet().rank("zs:a", "b");
        //6. ZREVRANGE key start stop ：返回有序集中指定区间内的成员，通过索引，分数从高到底
        redisTemplate.opsForZSet().reverseRange("zs:a", 0, -1);
        //7. ZREM key member [member …] 移除有序集合中的一个或多个成员
        redisTemplate.opsForZSet().remove("zs:a", "d");
        //8. ZREMRANGEBYRANK key start stop 移除有序集合中给定的排名区间的所有成员(第一名是0)(低到高排序）
        redisTemplate.opsForZSet().removeRange("zs:a", 1, 2);
        //9. ZREMRANGEBYSCORE key min max 移除有序集合中给定的分数区间的所有成员
        redisTemplate.opsForZSet().removeRangeByScore("zs:a", 1, 2);
    }
}
