package com.aaa.commondevelop.domain.annotation;

import com.aaa.commondevelop.config.aop.aspect.ParamConstraintValidated;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/3/12
 */
@Target({ ElementType.FIELD})
//只允许用在类的字段上
@Retention(RetentionPolicy.RUNTIME)
//注解保留在程序运行期间，此时可以通过反射获得定义在某个类上的所有注解
@Constraint(validatedBy = ParamConstraintValidated.class)
public @interface Check {
    /**
     * 合法的参数值
     **/
    String[] paramValues();

    /**
     * 提示信息
     **/
    String message() default "{custom.check.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
