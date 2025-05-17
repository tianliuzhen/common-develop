package com.aaa.commondevelop.config.redis.lock;

import com.aaa.commondevelop.config.redis.DelayTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * redisTemplate 基于 lua  实现分布式锁 版本一
 * 缺陷
 *   1、无法续约
 * @author liuzhen.tian
 * @version 1.0 RedisTemplateLock.java  2020/9/14 11:21
 */
@Slf4j
@Component
public class RedisLuaLockImpl implements RedisLuaLock{

    @Autowired
    private RedisTemplate redisTemplate;

    private DefaultRedisScript<Boolean> tryLockScript;

    private DefaultRedisScript<Boolean> releaseLockScript;

    private DefaultRedisScript<Boolean> delayScript;

    /**
     *默认加锁时间 10s
     */
    public static final Integer DEFAULT_TIME =10;

    @PostConstruct
    public void initLUA() {
        tryLockScript = new DefaultRedisScript();
        tryLockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("luascript/lock.lua")));
        tryLockScript.setResultType(Boolean.class);

        releaseLockScript = new DefaultRedisScript();
        releaseLockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("luascript/unlock.lua")));
        releaseLockScript.setResultType(Boolean.class);

        delayScript = new DefaultRedisScript();
        delayScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("luascript/delay.lua")));
        delayScript.setResultType(Boolean.class);
    }

    @Override
    public Boolean tryLock(String key, String value) {
        return tryLock( key, value, DEFAULT_TIME);
    }

    /**
     * 获取lua结果
     * @param key   key
     * @param value 值用于解锁时判断
     * @param time  默认单位是 秒
     * @return Boolean
     */
    @Override
    public Boolean tryLock(String key,String value,Integer  time) {

        // 封装参数
        List<String> keyList = Arrays.asList(key,String.valueOf(time),value);
        Boolean result= (Boolean)redisTemplate.execute(tryLockScript, keyList);
        // 使用下面的也可以，下面这个是基于 事务也能保证原子性，出于效率问题，还是使用lua 进行加锁。
        // Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, Long.parseLong(time), TimeUnit.SECONDS);
        log.info("redis set result："+result);
        if (result) {
            // 加锁成功, 启动一个延时线程, 防止业务逻辑未执行完毕就因锁超时而使锁释放
            Thread thread = new Thread(new DelayTask(key, value, time,this));
            thread.setDaemon(Boolean.TRUE);
            thread.start();
        }
        return result;
    }

    @Override
    public void lock(String key, String value) {
    }

    @Override
    public void lock(String key, String value, Integer time) {
    }

    /**
     * 释放锁操作
     * @param key
     * @param value
     * @return
     */
    @Override
    public Boolean releaseLock(String key, String value) {
        // 封装参数
        List<Object> keyList = new ArrayList();
        keyList.add(key);
        keyList.add(value);
        Boolean result = (Boolean) redisTemplate.execute(releaseLockScript, keyList);
        return result;
    }

    @Override
    public Boolean delayTask(String key, String value, Integer time) {
        List<String> keyList = Arrays.asList(key,String.valueOf(time),value);
        Boolean result= (Boolean)redisTemplate.execute(delayScript, keyList);
        return result;
    }
}
