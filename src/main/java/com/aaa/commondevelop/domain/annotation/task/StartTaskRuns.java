package com.aaa.commondevelop.domain.annotation.task;

import java.lang.annotation.*;

/**
 * @author liuzhen.tian
 * @version 1.0 StartTaskRuns.java  2022/9/29 20:05
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface StartTaskRuns {

    StartTaskRun[] value();
}
