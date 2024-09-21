package com.aaa.commondevelop.annotation.aspect;

import com.aaa.commondevelop.annotation.task.StartTaskRun;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author liuzhen.tian
 * @version 1.0 StartTaskRunAspect.java  2022/9/29 20:05
 */
@Aspect
@Component
public class StartTaskRunAspect {


    @Around("@within(com.aaa.commondevelop.annotation.task.StartTaskRun) || @annotation(com.aaa.commondevelop.annotation.task.StartTaskRuns)")
    public void startTask(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        StartTaskRun[] annotations = method.getAnnotationsByType(StartTaskRun.class);
        for (StartTaskRun annotation : annotations) {
            System.out.println(annotation.businessType());
        }
        System.out.println("执行开始");

        joinPoint.proceed();

        System.out.println("执行结束");
    }
}

