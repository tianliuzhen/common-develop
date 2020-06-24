package com.aaa.mybatisplus.annotation.aspect;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    @Pointcut("execution(* com.aaa.mybatisplus.web.UserController.aopAdd10(..))")
    public void aspectPrintTest() {
    }

    /**
     * case1.  修改方法参数返回值
     *                     **注：需要保证类型一致*
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("aspectPrintTest()")
    public int test1(ProceedingJoinPoint point) throws Throwable {
        int i = 0;
        Object[] args = point.getArgs();
        for (Object object : args) {
            System.out.println("切面输出:" + object);
            args[i] = (Object) ((int) object + 10);
        }
        return (int) point.proceed(args);

    }
    /**
     * case2.  修改方法返回值
     *                 **注1：    需要保证类型一致*
     *                 **注2：   如果配置的拦截的方法，是实际方法调用的其他方法，则此方法无效。
     *
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.aaa.mybatisplus.web.UserController.aopChangeReturn2(..))")
    public Object test2(ProceedingJoinPoint point) throws Throwable {
        String methodName = point.getSignature().getName();
        log.info("{} :开始执行----",methodName);
        Object newStr = "新的返回值";
        //改变返回值
        log.info("{} :执行结束----",methodName);
        // 让目标方法继续往下执行
        point.proceed();
        return newStr;

    }

    /**
     *  case3.  同时修改方法的参数值和返回值
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.aaa.mybatisplus.web.UserController.aopChangeReturn(..))")
    public Object test3(ProceedingJoinPoint point) throws Throwable {
        String methodName = point.getSignature().getName();
        log.info("{} :开始执行----",methodName);
        //运行doSth()，返回值用一个Object类型来接收
        Object newStr = "新的返回值";
        //改变返回值
        log.info("{} :执行结束----",methodName);
        // 让目标方法继续往下执行
        /**
         * 这里的  point.proceed() 有两个，
         *     1. Object proceed() throws Throwable;               一个无参（用于返回方法的返回值）
         *
         *     2. Object proceed(Object[] var1) throws Throwable;  一个有参（用于返回方法的参数值）
         *      如果此方法，满足 【point.proceed(args);】
         *                且同时 【return  返回值 ;】
         *                即可满足同时改变，参数和返回值。
         *
         */
        int i = 0;
        Object[] args = point.getArgs();
        for (Object object : args) {
            System.out.println("切面输出:" + object);
            args[i] = (Object) ("新的参数值");
        }
        point.proceed(args);
        return newStr;

    }
}
