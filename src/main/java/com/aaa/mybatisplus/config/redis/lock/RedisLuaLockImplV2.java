package com.aaa.mybatisplus.config.redis.lock;

import com.aaa.mybatisplus.config.redis.DelayTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

/**
 *  redisTemplate 基于 lua  实现分布式锁 版本二
 * @author liuzhen.tian
 * @version 1.0 RedisLock.java  2020/9/15 19:54
 */
@Component
public class RedisLuaLockImplV2 implements RedisLuaLock   {

    public static final int DEFAULT_SECOND_LEN = 10;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String LOCK_LUA =
            " if redis.call('setnx', KEYS[1], ARGV[1]) == 1" +
            " then redis.call('expire', KEYS[1], ARGV[2]) " +
            " return 'true' else return 'false' end";
    private static final String UNLOCK_LUA =
            " if redis.call('get', KEYS[1]) == ARGV[1] " +
            " then redis.call('del', KEYS[1]) " +
            " end" +
            " return 'true' ";

    /**
     * 延长锁超时的脚本语句和释放锁的Lua脚本类似
     */
    private static final String DELAY_LUA =
            " if redis.call('get', KEYS[1]) == ARGV[1] " +
            " then redis.call('expire', KEYS[1], ARGV[2]) " +
            " return 'true' else return 'false' end";

    private RedisScript lockRedisScript;
    private RedisScript unLockRedisScript;
    private RedisScript delayRedisScript;

    private RedisSerializer<String> argsSerializer;
    private RedisSerializer<String> resultSerializer;

    /**
     * 初始化lua 脚本
     */
    @PostConstruct
    public void init() {
        argsSerializer = RedisSerializer.string();
        resultSerializer = RedisSerializer.string();

        lockRedisScript = RedisScript.of(LOCK_LUA, String.class);
        unLockRedisScript = RedisScript.of(UNLOCK_LUA, String.class);
        delayRedisScript = RedisScript.of(DELAY_LUA, String.class);
    }


    @Override
    public Boolean tryLock(String lock, String val) {
        return this.tryLock(lock, val, DEFAULT_SECOND_LEN);
    }

    @Override
    public Boolean tryLock(String lock, String val, Integer second) {
        List<String> keys = Collections.singletonList(lock);
        String flag = redisTemplate.execute(lockRedisScript, argsSerializer, resultSerializer, keys, val, String.valueOf(second));
        boolean parseBoolean = Boolean.parseBoolean(flag);
        if(parseBoolean){
            // 加锁成功, 启动一个延时线程, 防止业务逻辑未执行完毕就因锁超时而使锁释放
            DelayTask postponeTask = new DelayTask(lock, val, second,this);
            Thread thread = new Thread(postponeTask);
            thread.setDaemon(Boolean.TRUE);
            thread.start();
        }
        return parseBoolean;
    }

    @Override
    public Boolean releaseLock(String lock, String val) {
        List<String> keys = Collections.singletonList(lock);
        String flag = redisTemplate.execute(unLockRedisScript, argsSerializer, resultSerializer, keys, val);
        return Boolean.valueOf(flag);
    }

    @Override
    public Boolean delayTask(String lock, String val, Integer second) {
        List<String> keys = Collections.singletonList(lock);
        String flag =   redisTemplate.execute(delayRedisScript, argsSerializer, resultSerializer, keys, val, String.valueOf(second));
        return Boolean.valueOf(flag);
    }
}
