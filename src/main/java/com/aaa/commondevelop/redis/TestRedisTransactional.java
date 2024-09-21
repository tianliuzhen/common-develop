package com.aaa.commondevelop.redis;

import com.aaa.commondevelop.config.redis.RedisDbMany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * @author liuzhen.tian
 * @version 1.0 TestRedisChangeDb.java  2020/10/14 11:11
 */
@RestController
public class TestRedisTransactional {
    private ValueOperations<String, String> valueOperations;
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisDbMany redisDbMany;


    @PostConstruct
    public void init() {
        redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisDbMany.getLettuceConnectionFactoryByIndex(6));
        redisTemplate.afterPropertiesSet();
        valueOperations = redisTemplate.opsForValue();
    }

    /**
     * 不借助spring，redis单独执行事务
     * @return
     */
    @GetMapping(value = "/incrementWithTrans")
    public Object incrementWithTrans() {
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.multi();
        Long increment = valueOperations.increment("test:count", 1);
        redisTemplate.exec();
        return increment;
    }

    /**
     * 切记：如果加上@Transactional，相当于 multi() =》... =》exec()
     * @return
     */
    @GetMapping(value = "/incrementWithTransV2")
    @Transactional
    public Object incrementWithTransV2() {
        redisTemplate.setEnableTransactionSupport(true);
        Long increment = valueOperations.increment("test:count", 1);
        return increment;
    }

    /**
     * 不开启spring事务，只设置setEnableTransactionSupport 也是无效的
     * @return
     */
    @GetMapping(value = "/incrementWithTransV3")
    public Object incrementWithTransV3() {
        redisTemplate.setEnableTransactionSupport(true);
        Long increment = valueOperations.increment("test:count", 1);
        return increment;
    }


    @GetMapping(value = "/incrementNoTrans")
    public Object incrementNoTrans() {
        return valueOperations.increment("test:count", 1);
    }
}
