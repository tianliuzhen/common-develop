package com.aaa.mybatisplus.test.testtargetwithin;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/3/13
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value={CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE, PARAMETER, TYPE})
//@Inherited
public @interface Secure {
}

