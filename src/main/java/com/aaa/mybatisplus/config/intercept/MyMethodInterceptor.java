package com.aaa.mybatisplus.config.intercept;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author liuzhen.tian
 * @version 1.0 MyMethodInterceptor.java  2023/4/20 22:48
 */

public class MyMethodInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println(getClass()+"调用方法前");
        Object ret=invocation.proceed();
        System.out.println(getClass()+"调用方法后");
        return ret;
    }
}
