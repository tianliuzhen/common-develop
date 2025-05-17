package com.aaa.commondevelop.config.redis.lock;

import com.aaa.commondevelop.config.redis.DelayTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * redisTemplate 基于 lua  实现分布式锁 版本二
 * <p>
 * KEYS[1]：
 * <p>
 * 表示传递给Lua脚本的第一个键（key）。
 * <p>
 * 在Redis集群模式下，只有键参数（KEYS）会被用于计算哈希槽（hash slot），从而确定数据存储在哪个节点上。
 * <p>
 * 在分布式锁的实现中，KEYS[1]通常是锁的名称（即锁的key），比如"my_lock"。
 * <p>
 * ARGV[1]：
 * <p>
 * 表示传递给Lua脚本的第一个非键参数（argument）。
 * <p>
 * 这些参数不会参与哈希槽的计算，通常用于传递值或其他辅助信息。
 * <p>
 * 在分布式锁的实现中，ARGV[1]通常是锁的值（即锁的value），比如一个随机生成的UUID或客户端标识，用于确保只有锁的持有者才能释放锁。
 *
 * @author liuzhen.tian
 * @version 1.0 RedisLock.java  2020/9/15 19:54
 */
@Component
public class RedisLuaLockImplV2 implements RedisLuaLock {

    public static final int DEFAULT_SECOND_LEN = 10; // 默认超时时间


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

    private static final ScheduledExecutorService scheduledExecutorService =
            Executors.newScheduledThreadPool(1);
    private final Map<String, ScheduledFuture<?>> renewalTasks = new ConcurrentHashMap<>();

    private static final int LOCK_RENEWAL_INTERVAL = 5; // 锁续期间隔时间(秒)


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
        if (parseBoolean) {
            // 加锁成功, 启动一个延时线程, 防止业务逻辑未执行完毕就因锁超时而使锁释放
            DelayTask postponeTask = new DelayTask(lock, val, second, this);
            startLockRenewal(lock, val, second);
        }
        return parseBoolean;
    }

    /**
     * 优化续约线程续命问题，在多线程情况下，守护线程可能会导致无法释放
     * Thread thread = new Thread(new DelayTask(key, value, time,this));
     * thread.setDaemon(Boolean.TRUE);
     * thread.start();
     *
     * @param lock
     * @param val
     * @param originalExpire
     */
    private void startLockRenewal(String lock, String val, Integer originalExpire) {
        // 计算续期间隔，确保在锁过期前续期
        int renewalInterval = Math.min(originalExpire / 3, LOCK_RENEWAL_INTERVAL);

        // 使用AtomicBoolean作为停止标志
        AtomicInteger delayCount = new AtomicInteger(5);

        ScheduledFuture<?> future = scheduledExecutorService.scheduleAtFixedRate(() -> {
            if (delayCount.get() == 0) {
                return;
            }

            try {
                if (delayTask(lock, val, originalExpire)) {
                    System.out.println("延时成功..................................." + new Date());
                    delayCount.getAndDecrement();
                }
            } catch (Exception e) {
                // 处理续期任务中的异常
                delayCount.set(0);
            }
        }, renewalInterval, renewalInterval, TimeUnit.SECONDS);

        // 记录续期任务
        renewalTasks.put(lock, future);
    }

    private void stopLockRenewal(String lock) {
        ScheduledFuture<?> future = renewalTasks.get(lock);
        if (future != null) {
            future.cancel(true);
            renewalTasks.remove(lock);
        }
    }


    @Override
    public Boolean releaseLock(String lock, String val) {
        List<String> keys = Collections.singletonList(lock);

        // 停止续期任务
        stopLockRenewal(lock);

        String flag = redisTemplate.execute(unLockRedisScript, argsSerializer, resultSerializer, keys, val);
        return Boolean.valueOf(flag);
    }

    @Override
    public Boolean delayTask(String lock, String val, Integer second) {
        List<String> keys = Collections.singletonList(lock);
        String flag = redisTemplate.execute(delayRedisScript, argsSerializer, resultSerializer, keys, val, String.valueOf(second));
        return Boolean.valueOf(flag);
    }


}
