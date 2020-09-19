package com.aaa.mybatisplus.config.redis.lockRedisson;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * @author liuzhen.tian
 * @version 1.0 DistributedLocker.java  2020/9/16 16:59
 */
public interface DistributedLocker {

    RLock lock(String lockKey);

    RLock lock(String lockKey, int timeout);

    RLock lock(String lockKey, TimeUnit unit, int timeout);

    boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime);

    void unlock(String lockKey);

    void unlock(RLock lock);
}
