package com.aaa.mybatisplus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liuzhen.tian
 * @version 1.0 PageAoDefault.java  2020/11/5 15:15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
public @interface PageAoDefault {
    String[] orderBy() default "";

    String direction() default "desc";

    int pageIndex() default 1;

    int pageSize() default 10;
}
