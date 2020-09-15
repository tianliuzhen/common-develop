package com.aaa.mybatisplus.config.redis.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author liuzhen.tian
 * @version 1.0 RedisLock.java  2020/9/15 19:54
 */
@Component
public class RedisLuaLockImplV2 implements RedisLuaLock   {

    public static final int DEFAULT_SECOND_LEN = 10;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String LOCK_LUA =
            "if redis.call('setnx', KEYS[1], ARGV[1]) == 1" +
            " then redis.call('expire', KEYS[1], ARGV[2]) " +
            "return 'true' else return 'false' end";
    private static final String UNLOCK_LUA =
            "if redis.call('get', KEYS[1]) == ARGV[1] " +
            "then redis.call('del', KEYS[1]) " +
            "end" +
            " return 'true' ";

    private RedisScript lockRedisScript;
    private RedisScript unLockRedisScript;

    private RedisSerializer<String> argsSerializer;
    private RedisSerializer<String> resultSerializer;

    /**
     * 初始化lua 脚本
     */
    @PostConstruct
    public void init() {
        argsSerializer = new StringRedisSerializer();
        resultSerializer = new StringRedisSerializer();

        lockRedisScript = RedisScript.of(LOCK_LUA, String.class);
        unLockRedisScript = RedisScript.of(UNLOCK_LUA, String.class);
    }


    @Override
    public Boolean tryLock(String lock, String val) {
        return this.tryLock(lock, val, DEFAULT_SECOND_LEN);
    }

    @Override
    public Boolean tryLock(String lock, String val, Integer second) {
        List<String> keys = Collections.singletonList(lock);
        String flag = redisTemplate.execute(lockRedisScript, argsSerializer, resultSerializer, keys, val, String.valueOf(second));
        return Boolean.valueOf(flag);
    }

    @Override
    public Boolean releaseLock(String lock, String val) {
        List<String> keys = Collections.singletonList(lock);
        String flag = redisTemplate.execute(unLockRedisScript, argsSerializer, resultSerializer, keys, val);
        return Boolean.valueOf(flag);
    }


}
