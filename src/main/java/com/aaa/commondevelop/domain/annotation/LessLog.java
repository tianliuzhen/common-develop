package com.aaa.commondevelop.domain.annotation;

import com.aaa.commondevelop.domain.enums.LogType;

import java.lang.annotation.*;

/**
 * @Author: Sawyer
 * @Description: 忽略日志的注解
 * @Date: Created in 2:40 PM 2019/8/14
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LessLog {
    /**
     * 默认不忽略日志
     *
     * @return
     */
    LogType type() default LogType.NONE;
}

