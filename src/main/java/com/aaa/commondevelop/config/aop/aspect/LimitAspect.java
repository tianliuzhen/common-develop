package com.aaa.commondevelop.config.aop.aspect;

/**
 * @author liuzhen.tian
 * @version 1.0 CurrentAspect.java  2020/9/11 14:50
 */

import com.aaa.commondevelop.domain.annotation.Limit;
import com.aaa.commondevelop.config.global.exceptions.LimitException;
import com.aaa.commondevelop.domain.enums.LtTypeEnum;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 *
 *  限流切面实现
 *
 */
@Slf4j
@Aspect
@Configuration
public class LimitAspect {
    private static final String UNKNOWN = "unknown";

    @Autowired
    private  RedisTemplate redisTemplate;

    DefaultRedisScript<Number> redisLUAScript;

    /***
     * redis Lua 限流脚本
     */
    private final static String LUA_LIMIT_SCRIPT =
            "local c = redis.call('get',KEYS[1]) \n" +
            "if c and tonumber(c) > tonumber(ARGV[1]) " +
            "then return c;\n" +
            "end c = redis.call('incr',KEYS[1])\n" +
            "if tonumber(c) == 1 then \n" +
            "redis.call('expire',KEYS[1],ARGV[2]) " +
            "end return c;";

    @PostConstruct
    public void initLUA() {
        redisLUAScript = new DefaultRedisScript<>();
        redisLUAScript.setScriptText(LUA_LIMIT_SCRIPT);
        redisLUAScript.setResultType(Number.class);

    }

    /**
     * 配置切面
     * */
    @Around("@within(com.aaa.commondevelop.domain.annotation.Limit) || @annotation(com.aaa.commondevelop.domain.annotation.Limit)")
    public Object interceptor(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Limit CurrentAnnotation = method.getAnnotation(Limit.class);
        LtTypeEnum ltTypeType = CurrentAnnotation.LtType();
        String name = ltTypeType.name();
        String key;
        // 给定的时间范围 单位(秒)
        int currentPeriod = CurrentAnnotation.period();
        // 一定时间内最多访问次数
        int currentCount = CurrentAnnotation.counts();

        /**
         * 根据限流类型获取不同的key ,如果不传我们会以方法名作为key
         */
        switch (ltTypeType) {
            case IP:
                key = getIpAddress();
                break;
            case KEY:
                key = CurrentAnnotation.keys();
                break;
            default:
                key = StringUtils.upperCase(method.getName());
        }

        ImmutableList<String> keys = ImmutableList.of(StringUtils.join(CurrentAnnotation.prefixs(), key));
        try {
            Number count = (Number) redisTemplate.execute(redisLUAScript, keys, currentCount, currentPeriod);
            log.info("Access try count is {} for name={} and key = {}", count, name, key);
            if (count != null && count.intValue() <= currentCount) {
                return pjp.proceed();
            } else {
                throw new LimitException("不好意思，你被限流了...");
            }
        } catch (Throwable e) {
            if (e instanceof RuntimeException) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
            throw new RuntimeException("服务器异常");
        }
    }

    /***
     * 获取id地址
     * @return ip
     */
    private String getIpAddress() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }



}

