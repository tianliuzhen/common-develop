package com.aaa.mybatisplus.config.redis;

import com.aaa.mybatisplus.config.redis.lock.RedisLuaLock;
import com.aaa.mybatisplus.config.redis.lock.RedisLuaLockImpl;

import java.util.concurrent.TimeUnit;

/**
 * @author liuzhen.tian
 * @version 1.0 PostponeTask.java  2020/12/17 17:05
 */

public class PostponeTask implements Runnable {

    private String key;
    private String value;
    private int expireTime;
    private boolean isRunning;
    private RedisLuaLock redisLuaLock;

    public PostponeTask() {
    }

    public PostponeTask(String key, String value, int expireTime, RedisLuaLockImpl redisLuaLock) {
        this.key = key;
        this.value = value;
        this.expireTime = expireTime;
        this.isRunning = Boolean.TRUE;
        this.redisLuaLock = redisLuaLock;
    }

    @Override
    public void run() {
        int waitTime = expireTime * 1000 * 2 / 3;// 线程等待多长时间后执行
        while (isRunning) {
            try {
                Thread.sleep(waitTime);
                // TimeUnit.MILLISECONDS.sleep(waitTime);
                if (redisLuaLock.tryLock(key, value, expireTime)) {
                    System.out.println("延时成功...................................");
                } else {
                    this.stop();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void stop() {
        this.isRunning = Boolean.FALSE;
    }

}
