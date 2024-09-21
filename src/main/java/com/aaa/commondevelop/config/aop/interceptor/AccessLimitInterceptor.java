package com.aaa.commondevelop.config.aop.interceptor;

/**
 * @author liuzhen.tian
 * @version 1.0 FangshuaInterceptor.java  2020/9/11 17:43
 */
import com.aaa.commondevelop.domain.annotation.AccessLimit;
import com.aaa.commondevelop.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class  AccessLimitInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断请求是否属于方法的请求
        if(handler instanceof HandlerMethod){

            HandlerMethod hm = (HandlerMethod) handler;

            //获取方法中的注解,看是否有该注解
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if(accessLimit == null){
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean login = accessLimit.needLogin();
            String key = request.getRequestURI();
            //如果需要登录
            if(login){
                //获取登录的session进行判断
                key+=""+"1";  //这里假设用户是1,项目中是动态获取的userId
            }

            String ip1 = CommonUtils.getIpAddr(request);
            //从redis中获取用户访问的次数
            Object ip = redisTemplate.opsForValue().get(ip1);
            int count = ip == null ? 0 : Integer.parseInt(ip.toString());
            if(count == 0){
                //第一次访问
                redisTemplate.opsForValue().set(ip1,1,seconds);
                redisTemplate.expire(ip1, seconds, TimeUnit.SECONDS);
            }else if(count < maxCount){
                //加1
                redisTemplate.opsForValue().increment(ip1,count);
            }else{
                //超出访问次数
                log.error("超出访问次数");
                return false;
            }
        }

        return true;

    }
}

