package com.aaa.commondevelop.domain.annotation;

import java.lang.annotation.*;

/**
 * @author liuzhen.tian
 * @version 1.0 Lock.java  2022/8/8 20:43
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Lock {
    String prefix();

    String suffix();
}
