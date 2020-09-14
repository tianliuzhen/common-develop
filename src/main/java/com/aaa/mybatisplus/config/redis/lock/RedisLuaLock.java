package com.aaa.mybatisplus.config.redis.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * redisTemplate 基于 lua  实现分布式锁
 * 缺陷
 *   1、仅redis 可用
 *   2、无法续约
 * @author liuzhen.tian
 * @version 1.0 RedisTemplateLock.java  2020/9/14 11:21
 */
@Slf4j
@Component
public class RedisLuaLock {
    @Autowired
    private RedisTemplate redisTemplate;

    private DefaultRedisScript<Boolean> lockScript;

    private DefaultRedisScript<Boolean> releaseLockScript;

    @PostConstruct
    public void initLUA() {
        lockScript = new DefaultRedisScript();
        lockScript.setScriptText(LUA_LOCK_SCRIPT);
        lockScript.setResultType(Boolean.class);

        releaseLockScript = new DefaultRedisScript();
        releaseLockScript.setScriptText(LUA_RELEASE_LOCK_SCRIPT);
        releaseLockScript.setResultType(Boolean.class);
    }

    /**
     * 获取lua结果
     * @param key   key
     * @param value 值用于解锁时判断
     * @param time  默认单位是 秒
     * @return Boolean
     */
    public Boolean tryLock(String key,String value,String  time) {
        // 封装参数
        List<Object> keyList = new ArrayList();
        keyList.add(key);
        keyList.add(time);
        keyList.add(value);
        Boolean result= (Boolean)redisTemplate.execute(lockScript, keyList);
        log.info("redis set result："+result);
        return result;
    }

    /**
     * 释放锁操作
     * @param key
     * @param value
     * @return
     */
    public boolean releaseLock(String key, String value) {
        // 封装参数
        List<Object> keyList = new ArrayList();
        keyList.add(key);
        keyList.add(value);
        Boolean result = (Boolean) redisTemplate.execute(releaseLockScript, keyList);
        return result;
    }
    /***
     *  Lua 加锁脚本
     */
    private final static String LUA_LOCK_SCRIPT =
            "local  lockKey   = KEYS[1]\n" +
            "local  lockTime  = KEYS[2]\n" +
            "local  lockValue = KEYS[3]\n" +
            "local result_1 = redis.call('SETNX', lockKey, lockValue)\n" +
            "if result_1 == 1 \n" +
            "then\n" +
            "local result_2 = redis.call('SETEX', lockKey,lockTime, lockValue)\n" +
            "return result_2 \n" +
            "else\n" +
            "return false\n" +
            "end";
    /***
     *  Lua 释放锁脚本
     */
    private final static String LUA_RELEASE_LOCK_SCRIPT =
            "local lockKey   = KEYS[1]\n" +
            "local lockValue = KEYS[2]\n" +
            "local result_1  = redis.call('get', lockKey)\n" +
            "if result_1 == lockValue\n" +
            "then\n" +
            "local result_2 = redis.call('del', lockKey)\n" +
            "return result_2\n" +
            "else\n" +
            "return false\n" +
            "end";
}
