package com.aaa.mybatisplus.annotation.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 *     该切面作用：
 *     由于 @args 无法直接操作 参数注解，如果想要aop实现修改 修改方法注解的话，可参考如下
 *     或者配合 自定义注解 进一步进行改动和优化。
 * @author liuzhen.tian
 * @version $ Id: AspectTest.java, v 0.1 2020/6/24 14:33 liuzhen.tian Exp $
 */

@Aspect
@Component
public class AspectTest {

    /**
     * execution表达式简单说明
     * 1、第一种：表示在UserMangeImpl类下的所有方法，这种方式在之前的案例使用的。
     *     @Pointcut("execution(* com.spring.two.UserMangeImpl.*(..))")
     * 2、 第二种：表示任意公共的方法，其他修饰符也同样的道理。
     *     @Pointcut("execution(public * *(..))")
     * 3、第三种：任何一个以set开始的方法，不仅仅是set，save、update也都行。
     *     @Pointcut("execution(* set*(..))")
     * 4、第四种：在指定包下的所有方法
     *     @Pointcut("execution(* com.spring.*.*(..))")
     */

    /** 表示拦截在 UserController类下的findByIdAndAdd10方法 */
    @Pointcut("execution(* com.aaa.mybatisplus.web.UserController.findByIdAndAdd10(..))")
    public void aspectPrintTest() {
    }

    /**
     * 修改参数返回值
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("aspectPrintTest()")
    public int test1(ProceedingJoinPoint point) throws Throwable {
        System.out.println("aspectPrintTest()");
        int i = 0;
        Object[] args = point.getArgs();
        for (Object object : args) {
            System.out.println("切面输出:" + object);
            args[i] = (Object) ((int) object + 10);
        }

        return (int) point.proceed(args);

    }

}
