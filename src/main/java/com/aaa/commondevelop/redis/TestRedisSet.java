package com.aaa.commondevelop.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * description: 这里类似 java的 HashSet
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/3/22
 */
@RestController
public class TestRedisSet {
    @Autowired
    RedisTemplate redisTemplate;

    @PostMapping("/testRedisSet")
    public void testRedisSet(){
        //1. sadd key value1[value2]：向集合添加成员
        redisTemplate.opsForSet().add("s:a",1);
        redisTemplate.opsForSet().add("s:a",2);
        redisTemplate.opsForSet().add("s:a",3);
        redisTemplate.opsForSet().add("s:a",7);
        redisTemplate.opsForSet().add("s:a",8);
        redisTemplate.opsForSet().add("s:a",9);
        Long add = redisTemplate.opsForSet().add("s:a", 3);
        //2. scard key：返回集合成员数
        Set members = redisTemplate.opsForSet().members("s:a");
        //3. sismember key member：判断memeber元素是否是集合key成员的成员
        redisTemplate.opsForSet().isMember("s:a", 1);
        //4. srandmember key [count]：返回集合中一个或多个随机数
        redisTemplate.opsForSet().randomMembers("s:a", 1);
        //5. srem key member1 [member2]：移除集合中一个或多个成员
        redisTemplate.opsForSet().remove("s:a", 1, 2);
        //6. spop key：移除并返回集合中的一个或者几个随机元素
        List pop = redisTemplate.opsForSet().pop("s:a", 1);
        //7.   smove source destination member：将member元素从source集合移动到destination集合
        Boolean move = redisTemplate.opsForSet().move("s:a", 9, "s:anew");
        //8. sdiff key1 [key2]：返回所有集合的差集
        Set difference = redisTemplate.opsForSet().difference("s:a", "s:anew");
        //9. sdiffstore destination key1[key2]：返回给定所有集合的差集并存储在destination中
        redisTemplate.opsForSet().differenceAndStore("s:a", "s:anew", "s:destkey");
    }
}
