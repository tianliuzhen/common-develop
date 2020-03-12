package com.aaa.mybatisplus.annotation.aspect;

import com.aaa.mybatisplus.annotation.SysLog;
import com.aaa.mybatisplus.entity.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/3/12
 */
@Slf4j
@Aspect
@Component
public class AssignmentAspect {

    @Around("@annotation(com.aaa.mybatisplus.annotation.Assignment)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        Class<?> clazz = joinPoint.getTarget().getClass();
        //通过反射创建对象
        Object object = clazz.newInstance();
        //获取 str 字段
        Field field = clazz.getDeclaredField("str");
        //暴力反射
        field.setAccessible(true);
        field.set(object,"new_str");
        System.out.println(field.get(object));


        //获取 对象 字段
        Class<?> aClass = Class.forName("com.aaa.mybatisplus.entity.UserDto");
        Object aObject = aClass.newInstance();
        Field field1 = aClass.getDeclaredField("name");
        field1.setAccessible(true);
        field1.set(aObject,"new_name");
        System.out.println(field1.get(aObject));

        Object result = joinPoint.proceed();
        return result;
    }
}
