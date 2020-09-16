package com.aaa.mybatisplus.web;

import com.aaa.mybatisplus.annotation.AccessLimit;
import com.aaa.mybatisplus.annotation.LessLog;
import com.aaa.mybatisplus.annotation.Limit;
import com.aaa.mybatisplus.domain.enums.LogType;
import com.aaa.mybatisplus.domain.enums.LtTypeEnum;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限流测试
 * @author liuzhen.tian
 * @version 1.0 GuavaLimitRateLimterController.java  2020/9/11 10:31
 */
@Slf4j
@RestController
@RequestMapping(value = "/LimitRateTest")
public class RateLimiterController {


    //创建一个限流器，参数代表每秒生成的令牌数(用户限流频率设置 每秒中限制1个请求)
    private RateLimiter rateLimiter = RateLimiter.create(1);


    @LessLog(type = LogType.ALL)
    @GetMapping("/GuavaLimitRateLimiterTest")
    public String test(HttpServletRequest request){
        //设置等待超时时间的方式获取令牌，如果超timeout为0，则代表非阻塞，获取不到立即返回
        boolean tryAcquire = rateLimiter.tryAcquire(0, TimeUnit.SECONDS);
        if (!tryAcquire) {
            return "现在抢购的人数过多，请稍等一下哦!";
        }
        return "抢购排队中...";
    }

    //模拟两个计数
    private static final AtomicInteger ATOMIC__1 = new AtomicInteger();
    private static final AtomicInteger ATOMIC__2 = new AtomicInteger();

    @Limit(keys = "currentTest1", period = 10, counts= 3)
    @GetMapping("/currentTest1")
    public int testLimiter1() {

        return ATOMIC__1.incrementAndGet();
    }


    @Limit(keys = "customer_limit_test", period = 10, counts = 2, LtType = LtTypeEnum.IP)
    @GetMapping("/currentTest2")
    public int testLimiter2() {
        return ATOMIC__2.incrementAndGet();
    }

    /**
     * 防止刷接口 seconds=5 秒内，允许请求  maxCount=5
     */
    @AccessLimit(seconds=5, maxCount=5, needLogin=true)
    @GetMapping("/testAccessLimit")
    public void  testAccessLimit(){

    }
}
