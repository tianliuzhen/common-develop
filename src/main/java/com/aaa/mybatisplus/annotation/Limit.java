package com.aaa.mybatisplus.annotation;

import com.aaa.mybatisplus.domain.enums.LtTypeEnum;

import java.lang.annotation.*;

/**
 *  自定义限流注解
**/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Limit {

    /**
     * 名字
     */
    String names() default "";

    /**
     * key
     */
    String keys() default "";

    /**
     * Key的前缀
     */
    String prefixs() default "";

    /**
     * 给定的时间范围 单位(秒)
     */
    int period();

    /**
     * 一定时间内最多访问次数
     */
    int counts();

    /**
     * 限流的类型(用户自定义key 或者 请求ip)
     */
    LtTypeEnum LtType() default LtTypeEnum.KEY;
}
