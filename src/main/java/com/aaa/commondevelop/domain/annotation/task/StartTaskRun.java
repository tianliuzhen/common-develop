package com.aaa.commondevelop.domain.annotation.task;

import java.lang.annotation.*;

/**
 * @author liuzhen.tian
 * @version 1.0 StartTaskRun.java  2022/9/29 20:04
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Repeatable(value = StartTaskRuns.class)
public @interface StartTaskRun {

    int businessType() default 0;

}
