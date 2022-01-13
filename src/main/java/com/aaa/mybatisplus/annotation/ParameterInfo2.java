package com.aaa.mybatisplus.annotation;

import java.lang.annotation.*;

/**
 * @author liuzhen.tian
 * @version 1.0 ParameterInfo2Interceptor.java  2022/1/13 21:43
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterInfo2 {
    String value() default "userId";//默认获取userId的值
}
