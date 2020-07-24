package com.aaa.mybatisplus.annotation;

import java.lang.annotation.*;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/6/23
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ArgsInfo {
    String value() default "userId";//默认获取userId的值
}
