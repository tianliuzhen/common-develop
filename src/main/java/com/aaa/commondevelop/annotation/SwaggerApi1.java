package com.aaa.commondevelop.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SwaggerApi1 {
    @AliasFor("groupName")
    String[] value() default {};

    @AliasFor("value")
    String[] groupName() default {};
}
