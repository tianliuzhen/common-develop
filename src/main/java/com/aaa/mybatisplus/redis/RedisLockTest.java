package com.aaa.mybatisplus.redis;

/**
 * @author liuzhen.tian
 * @version 1.0 RedisLockTest.java  2020/7/24 19:54
 */
import com.aaa.mybatisplus.config.redis.lock.RedisLockSet;
import redis.clients.jedis.Jedis;

import java.util.UUID;

public class RedisLockTest {


    /*    public static void main(String[] args) {
            Jedis jedis = RedisLockUtil.getJedis();
            String lockKey = "REDIS_LOCK_KEY";
            String value = UUID.randomUUID().toString();
            try{
                if(RedisLockUtil.getDistributedLock(jedis, lockKey, value, 20000)){
                    System.out.println("redis lock ......");
                }
            }catch (Exception e){
                System.out.println("redis lock exception ......");
            }finally {
                RedisLockUtil.releaseDistributedLock(jedis, lockKey, value);
            }
        }*/
    public static void main(String[] args) {
        MyThread Thread1=new MyThread();
        Thread mThread1=new Thread(Thread1,"线程1");
        Thread mThread2=new Thread(Thread1,"线程2");
        mThread1.start();
        mThread2.start();
    }
    public static class MyThread implements Runnable{
        @Override
        public void run() {
            for (int i = 0; i < 1; i++) {
                Jedis jedis = RedisLockSet.getJedis();
                String lockKey = "REDIS_LOCK_KEY";
                String value = UUID.randomUUID().toString();
                System.err.println("开始竞争锁"+value);
                try{
                    if(RedisLockSet.getDistributedLock(jedis, lockKey, value, 20000)){
                        System.out.println(Thread.currentThread().getName()+":：：：：当前进锁线程="+"Key："+value);
                        Thread.sleep(3000);
                        System.out.println("redis lock ......");
                    }
                }catch (Exception e){
                    System.out.println("redis lock exception ......");
                }finally {
                    RedisLockSet.releaseDistributedLock(jedis, lockKey, value);
                    System.err.println("释放锁"+value);
                }
                System.out.println(Thread.currentThread().getName()+":当前线程");


            }
        }
    }
}
