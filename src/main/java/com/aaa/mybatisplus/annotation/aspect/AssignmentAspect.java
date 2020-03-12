package com.aaa.mybatisplus.annotation.aspect;

import com.aaa.mybatisplus.util.ReflectUtils;
import com.aaa.mybatisplus.entity.UserDto;
import com.aaa.mybatisplus.web.BaseControllerImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
public class  AssignmentAspect {

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

        String methodName = joinPoint.getSignature().getName();
        if(methodName.equals("testInt")){
            BaseControllerImpl.userDto = new UserDto("name","man");
        }else{
            BaseControllerImpl.userDto = new UserDto("name2","woman");
        }


        Object result = joinPoint.proceed();
        return result;
    }
}
