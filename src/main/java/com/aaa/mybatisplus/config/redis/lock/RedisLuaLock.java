package com.aaa.mybatisplus.config.redis.lock;

/**
 * @author liuzhen.tian
 * @version 1.0 redisLuaLock.java  2020/9/15 19:20
 */
public interface RedisLuaLock {


    /**
     * 加锁
     *
     * @param key   key
     * @param value 值用于解锁时判断
     * @return Boolean
     */
    Boolean tryLock(String key, String value);

    /**
     * 自定义加锁过期时间
     *
     * @param key   key
     * @param value 值用于解锁时判断
     * @param time  默认单位是 秒
     * @return Boolean
     */
    Boolean tryLock(String key, String value, Integer time);

    /**
     * 释放锁操作
     *
     * @param key
     * @param value
     * @return
     */
    Boolean releaseLock(String key, String value);

    /**
     * 这个操作是给业务超时的时候，续约时间。
     *
     * @param key   key
     * @param value 值用于解锁时判断
     * @param time  默认单位是 秒
     * @return Boolean
     */
    Boolean delayTask(String key, String value, Integer time);

}
