package com.aaa.commondevelop.config.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Modifier;

/**
 * 该切面作用：
 * 由于 @args 无法直接操作 参数注解，如果想要aop实现修改 修改方法注解的话，可参考如下
 * 或者配合 自定义注解 进一步进行改动和优化。
 *
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

    /**
     * 表示拦截在 UserController类下的findByIdAndAdd10方法
     */
    @Pointcut("execution(* com.aaa.commondevelop.web.UserController.aopAdd10(..))")
    public void aspectPrintTest() {
    }

    /**
     * 1、execution(* com.aaa.commondevelop.service..BaseService*.*(..))
     * <p>
     * 符号	                        |   含义
     * execution()                  |   表达式的主体；
     * 第一个 * 符号                 |   表示返回值的类型任意；
     * com.aaa.commondevelop.service  |	AOP所切的服务的包名，即，我们的业务部分
     * 包名后面的 ..                 |   表示当前包及子包
     * 第二个 *                      |   表示类名，*即所有类。此处可以自定义，下文有举例
     * .*(..)	                    |   表示任何方法名，括号表示参数，两个点表示任何参数类型
     * <p>
     * 2、execution(* com.aaa.commondevelop.service.impl..*BaseService*.*(..))
     * 表示拦截 com.aaa.commondevelop.service.impl包下面的 BaseService结尾的类
     * <p>
     * 注：BaseService的子类 [AaBaseService,BbBaseService]均能被拦截到
     */
    @Pointcut("execution(* com.aaa.commondevelop.service.impl..*BaseService*.*(..))")
    public void aspectExpression() {
    }

    /**
     * 环绕通知：测试表达式
     */
    @Around("aspectExpression()")
    public void testExpression(ProceedingJoinPoint point) throws Throwable {
        // 获取参数
        Object[] args = point.getArgs();

        // 输出拦截信息
        log.info("目标方法名为:" + point.getSignature().getName());
        log.info("目标方法所属类的简单类名:" +        point.getSignature().getDeclaringType().getSimpleName());
        log.info("目标方法所属类的类名:" + point.getSignature().getDeclaringTypeName());
        log.info("目标方法声明类型:" + Modifier.toString(point.getSignature().getModifiers()));
        log.info("被代理的对象:" + point.getTarget());
        log.info("代理对象自己:" + point.getThis());

        // 执行方法
        point.proceed(args);
    }

    /**
     * case1.  修改方法参数返回值
     * **注：需要保证类型一致*
     *
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
     * **注1：    需要保证类型一致*
     * **注2：   如果配置的拦截的方法，是实际方法调用的其他方法，则此方法无效。
     *
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.aaa.commondevelop.web.UserController.aopChangeReturn2(..))")
    public Object test2(ProceedingJoinPoint point) throws Throwable {
        String methodName = point.getSignature().getName();
        log.info("{} :开始执行----", methodName);
        Object newStr = "新的返回值";
        //改变返回值
        log.info("{} :执行结束----", methodName);
        // 让目标方法继续往下执行
        point.proceed();
        return newStr;

    }

    /**
     * case3.  同时修改方法的参数值和返回值
     *
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.aaa.commondevelop.web.UserController.aopChangeReturn(..))")
    public Object test3(ProceedingJoinPoint point) throws Throwable {
        String methodName = point.getSignature().getName();
        log.info("{} :开始执行----", methodName);
        //运行doSth()，返回值用一个Object类型来接收
        Object newStr = "新的返回值";
        //改变返回值
        log.info("{} :执行结束----", methodName);
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
