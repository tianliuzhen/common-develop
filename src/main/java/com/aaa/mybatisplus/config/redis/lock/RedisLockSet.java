package com.aaa.mybatisplus.config.redis.lock;

/**
 * @author liuzhen.tian
 * @version 1.0 RedisLockUtil.java  2020/7/24 19:54
 */
import java.util.Collections;
import redis.clients.jedis.Jedis;

public class RedisLockSet {
    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final Long RELEASE_SUCCESS = 1L;
    /**
     * 系统启动时初始化
     */
    public static Jedis getJedis() {
        Jedis jedis = null;
        if (jedis == null) {
            synchronized (RedisLockSet.class) {
                if (jedis == null) {
                    jedis = new Jedis();
                }
            }
        }
        return jedis;
    }
    /**
     * 获取分布式锁
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param value 请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public static boolean getDistributedLock(Jedis jedis, String lockKey, String value, int expireTime) {
        String result = jedis.set(lockKey, value, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
        if (LOCK_SUCCESS.equals(result)) {
            System.out.println("get redis distributed lock success ......");
            return true;
        }
        System.out.println("get redis distributed lock fail ......");
        return false;
    }

    /**
     * 释放分布式锁
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param value 请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributedLock(Jedis jedis, String lockKey, String value) {
        boolean result = false;
        if(jedis != null){
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object object = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(value));
            if (RELEASE_SUCCESS.equals(object)) {
                System.out.println("execute lua script success ......");
                return true;
            }
        }
        return false;
    }


    /**
     * @param key   锁名
     * @param value 锁值
     * @return
     */
    public static boolean releaseLock(String key, String value) {
        return value.equals(getJedis().get(key)) && getJedis().del(key).equals(RELEASE_SUCCESS);
    }
}
