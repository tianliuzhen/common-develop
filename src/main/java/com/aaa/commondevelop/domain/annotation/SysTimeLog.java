package com.aaa.commondevelop.domain.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysTimeLog {
    String value() default "";
}
