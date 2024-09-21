package com.aaa.commondevelop.config.redis;

import com.aaa.commondevelop.config.redis.lock.RedisLuaLock;
import org.springframework.stereotype.Component;


/**
 * 守护线程，异步续命
 * @author liuzhen.tian
 * @version 1.0 PostponeTask.java  2020/12/17 17:05
 */

@Component
public class DelayTask implements Runnable {

    private String key;
    private String value;
    private int expireTime;
    private boolean isRunning;

    private RedisLuaLock redisLuaLock;

    public DelayTask() {
    }

    public DelayTask(String key, String value, int expireTime,RedisLuaLock redisLuaLock ) {
        this.key = key;
        this.value = value;
        this.expireTime = expireTime;
        this.isRunning = Boolean.TRUE;
        this.redisLuaLock = redisLuaLock;
    }

    @Override
    public void run() {
        // int waitTime = expireTime * 1000 * 2 / 3;// 线程等待多长时间后执行
        int waitTime = expireTime * 1000 / 4;
        while (isRunning) {
            try {
                Thread.sleep(waitTime);
                // TimeUnit.MILLISECONDS.sleep(waitTime);
                if (redisLuaLock.delayTask(key, value, expireTime)) {
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
