package com.aaa.commondevelop.config.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;
import java.util.Arrays;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/3/12
 */
@Slf4j
@Aspect
@Order(value = 2)
@Component
public class SysLogAspect {

    /**
     * 这里我们使用注解的形式 当然，我们也可以通过切点表达式直接指定需要拦截的package,
     * 需要拦截的class 以及 method 切点表达式:
     * execution(...)
     */
    @Pointcut("@within(com.aaa.commondevelop.domain.annotation.SysLog) || @annotation(com.aaa.commondevelop.domain.annotation.SysLog)")
    public void logPointCut() { }


    @Before("logPointCut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        System.out.println("before : "+ LocalTime.now());
        //接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //记录请求内容
        log.info("请求url : "+request.getRequestURL().toString());log.info("HTTP_METHOD : " + request.getMethod());
        log.info("请求ip : " + request.getRemoteAddr());
        log.info("请求方法 : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("请求参数 : " + Arrays.toString(joinPoint.getArgs()));

    }

    /**
     * 环绕通知 @Around ， 当然也可以使用 @Before (前置通知) @After (后置通知)
     *
     * @param point
     * @return
     * @throws Throwable
     */
    //    @Around("logPointCut()")  // 都是可以的
    /**
     * 这里的意思是 注解 @SysLog  作用与方法上和类上
     */
    @Around("@within(com.aaa.commondevelop.domain.annotation.SysLog) || @annotation(com.aaa.commondevelop.domain.annotation.SysLog)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object result = point.proceed();
        long time = System.currentTimeMillis() - beginTime;
        try {
            System.out.println(time);
        } catch (Exception e) {
           e.printStackTrace();
        }
        return result;
    }

    @AfterReturning(returning = "ret", pointcut = "logPointCut()")
    public void doAfterReturning(Object ret) throws Throwable{
        System.out.println("after : "+ LocalTime.now());
        //处理完请求，返回内容
        log.info("RESPONCE : "+ ret);
    }

    /**
     * 操作发生异常记录日志
     */
    @AfterThrowing(pointcut = "logPointCut()", throwing = "exception")
    public void doAfterThrowing(JoinPoint joinPoint, Exception exception) {
        log.error("exception: "+ exception);
    }
}
