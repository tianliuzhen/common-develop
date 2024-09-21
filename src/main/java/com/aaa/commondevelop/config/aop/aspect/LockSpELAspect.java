package com.aaa.commondevelop.config.aop.aspect;

import com.aaa.commondevelop.domain.annotation.Lock;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * https://www.jianshu.com/p/02d91087a419
 * https://www.jianshu.com/p/14e54863faae
 *
 * @author liuzhen.tian
 * @version 1.0 LockAspect.java  2022/8/8 20:53
 */

@Aspect
@Component
public class LockSpELAspect {

    // 定义切点
    // @Pointcut("execution(* com.aaa.commondevelop.web..*.*(..))")
    @Pointcut("@within(com.aaa.commondevelop.domain.annotation.Lock) || @annotation(com.aaa.commondevelop.domain.annotation.Lock)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object pointCut(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //获取注解参数值
        Lock lock = methodSignature.getMethod().getAnnotation(Lock.class);
        String prefix = lock.prefix();
        String spELString = lock.suffix();
        // 通过joinPoint获取被注解方法
        String value = generateKeyBySpEL(spELString, joinPoint);
        // todo 解析后的参数可以继续做后续增加业务处理 例：加锁
        // todo 执行业务逻辑
        return joinPoint.proceed();
    }

    private String generateKeyBySpEL(String spELString, ProceedingJoinPoint joinPoint) {
        if (StringUtils.isBlank(spELString)) {
            return null;
        }
        // 通过joinPoint获取被注解方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        //创建解析器
        SpelExpressionParser parser = new SpelExpressionParser();
        //获取表达式
        Expression expression = parser.parseExpression(spELString);
        //设置解析上下文(有哪些占位符，以及每种占位符的值)
        EvaluationContext context = new StandardEvaluationContext();
        //获取参数值
        Object[] args = joinPoint.getArgs();
        //获取运行时参数的名称
        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        String[] parameterNames = discoverer.getParameterNames(method);
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }
        //解析,获取替换后的结果
        String result = expression.getValue(context).toString();
        System.out.println(result);
        return result;
    }
}
