package com.aaa.mybatisplus.redis;

import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonLock;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Repeatable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 虽然是面试 提到这三个问题，但是真的很重要呢
 * 1、redis 锁失效时间小于业务执行时间怎么处理
 * 2、redis 缓存击穿 解决方案
 *
 * @author liuzhen.tian
 * @version 1.0 TestRedisLock.java  2020/12/18 10:16
 */
@Slf4j
@RestController
public class TestRedisZLock {


    @Autowired
    @Qualifier("redissonClient")
    private RedissonClient redissonClient;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 测试 redisson
     */
    @GetMapping(value = "/lockByRedisson")
    public String lockByRedisson() throws InterruptedException {
        /**
         * Lock 方法测试
         * case1.
         *      lock()
         *      这个是java 自带的 lock，未抢到锁直接阻塞，业务业务超时一直阻塞。
         * case2.
         *      lock(5,TimeUnit.SECONDS);
         *      阻塞5秒内如果没获取锁，新的线程过来直接过来往下走。
         */
        System.out.println("==========lock()=============tryLock()======================");
        /**
         *  tryLock 方法测试
         * 下面都是经过 jmeter 压测分析的
         * case1.
         *      tryLock();
         *      这个是java 自带的 lock，这里未抢到锁，直接返回false
         * case2.
         *      tryLock(5, TimeUnit.SECONDS);
         *      指定的时间内尝试获取锁，如这里5秒内新过来的线程会处于一个阻塞的状态,
         *      如果5秒还没抢到锁，直接返回 false。
         *      tryLock(5, TimeUnit.SECONDS);
         * case3.
         *      tryLock(5,5, TimeUnit.SECONDS);
         *      和case2. 区别 多了一个参数，第二个参数是 5秒内强制释放锁，但是5秒内仍是阻塞的状态
         *      意思是 尝试加锁，最多等待3秒，上锁以后3秒自动解锁
         */
        RLock stock = redissonClient.getLock("stock");
        stock.lock(5,TimeUnit.SECONDS);
        if (true) {
            try {
                log.info("线程 id: " + Thread.currentThread().getId() + " 获得锁");
                //模型业务执行时间
                TimeUnit.SECONDS.sleep(10);
            }finally {
                stock.unlock();
                log.info("线程 id: " + Thread.currentThread().getId() + " 执行成功，执行解锁");
            }
            return "获取锁成功";
        }else {
            log.info("线程 id: " + Thread.currentThread().getId() + " 获取锁失败");
            return "获取锁失败";
        }
    }

    /**
     * 缓存击穿指的是，
     * redis 缓存的key 在某一个瞬间失效，成千上万的请求过来，在从redis 里面没查到的情况下，
     * 都去访问数据库，给数据库造成压力。
     * 解决方案：
     *        采用类似单例模式的，双重检查锁进行检查。
     *        参考:https://blog.csdn.net/hjl021/article/details/79168783
     */
    @GetMapping(value = "/redisBreakdown")
    public List<Integer> redisBreakdown(){
        // keyList 采用 volatile 修饰
        List<Integer> keyList = (List<Integer>)redisTemplate.opsForValue().get("key");
        if (keyList == null) {
            synchronized (this){
             //双重检测锁,假使同时有5个请求进入了上一个if(null == keyList),加了锁之后one by one 的访问,
              // 这里再次对缓存进行检测,尽一切可能防止缓存穿透的产生,但是性能会有所损失
                keyList = (List<Integer>)redisTemplate.opsForValue().get("key");
                if (keyList == null) {
                    //这里的 list 模拟从数据库查找的数据
                    List<Integer> list = Arrays.asList(1, 2, 3);
                    log.info("查找数据库");
                    redisTemplate.opsForValue().set("key",list,60*12,TimeUnit.SECONDS);
                    keyList = list;
                    System.out.println("请求的数据库。。。。。。");
                }else {
                    //System.out.println("请求的缓存。。。。。。");
                }
            }
        }
        return keyList;
    }

    @GetMapping(value = "/bloomFilter")
    public String redisBreakdown(@RequestParam(value = "") String phone){
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("phoneList");
        //初始化布隆过滤器：预计元素为100000000L,误差率为3%
        bloomFilter.tryInit(100000000L, 0.03);
        if (bloomFilter.contains(phone)) {
            return phone+" :已经存在";
        }else {
            bloomFilter.add(phone);
            return phone+" :不存在";
        }
    }
}
