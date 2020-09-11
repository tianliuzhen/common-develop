package com.aaa.mybatisplus.web;

import com.aaa.mybatisplus.annotation.LessLog;
import com.aaa.mybatisplus.enums.LogType;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author liuzhen.tian
 * @version 1.0 GuavaLimitRateLimterController.java  2020/9/11 10:31
 */
@RestController
public class GuavaLimitRateLimiterController {
    private static final Logger logger = LoggerFactory.getLogger(GuavaLimitRateLimiterController.class);
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
}
