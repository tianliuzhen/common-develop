package com.aaa.commondevelop.config.aop.aspect;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/1/14
 */

import com.aaa.commondevelop.domain.annotation.LessLog;
import com.aaa.commondevelop.domain.enums.LogType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * description: 日志记录请求参数 、url、返回值
 * 为了方便log观察，设置可忽略查看
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2019/12/18
 */
@Slf4j
@Aspect
// @Component
public class RequestLogAspect {

    @Autowired
    HttpServletRequest request;


    @Around("execution(* com.aaa.commondevelop.web..*.*(..))")
    public Object around(final ProceedingJoinPoint joinPoint) throws Throwable {

        boolean urlLogRequired = Boolean.TRUE;
        boolean requestLogRequired = Boolean.TRUE;
        boolean responseLogRequired = Boolean.TRUE;

        Class<?> clazz = joinPoint.getTarget().getClass();
        String methodName = joinPoint.getSignature().getName();
        Class<?>[] args = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        Method method = clazz.getMethod(methodName, args);

        if (method.isAnnotationPresent(LessLog.class)) {
            //减少日志的注解
            LessLog lessLog = method.getAnnotation(LessLog.class);
            LogType logType = lessLog.type();
            switch (logType) {
                case URL:
                    urlLogRequired = Boolean.FALSE;
                    break;
                case REQUEST:
                    requestLogRequired = Boolean.FALSE;
                    break;
                case RESPONSE:
                    responseLogRequired = Boolean.FALSE;
                    break;
                case ALL:
                    urlLogRequired = Boolean.FALSE;
                    requestLogRequired = Boolean.FALSE;
                    responseLogRequired = Boolean.FALSE;
                    break;
                default:
            }
        }
        //url日志
        if (urlLogRequired) {
            log.info("请求url:{}", request.getRequestURL().toString());
        }

        ObjectMapper mapper = new ObjectMapper();
        //请求日志
        if (requestLogRequired) {
            log.info("请求参数:{}", mapper.writeValueAsString(joinPoint.getArgs()));
        }
        Object result = joinPoint.proceed();
        //响应日志
        if (responseLogRequired) {
            log.info("请求返回:{}", mapper.writeValueAsString(result));
        }

        return result;
    }
}


