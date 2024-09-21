package com.aaa.commondevelop.domain.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author liuzhen.tian
 * @version 1.0 AccessLimit.java  2020/9/11 17:27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {

    /**
     * 失效时间
     */
    int seconds();
    /**
     * 失效时间内最大请求次数
     */
    int maxCount();
    /**
     * 是否需要验证登录
     */
    boolean needLogin()default true;
}
