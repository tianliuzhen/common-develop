package com.aaa.mybatisplus.test.testtargetwithin;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/3/13
 */
@Aspect
@Component
public class ExecutionAop {

    @Before("@within(com.aaa.mybatisplus.test.testtargetwithin.Secure)")
    public void execute1(){
        System.out.println("@within(com.learn.annotation.Secure)");
    }

    @Before("execution(* com.aaa..*(..)) && @target(com.aaa.mybatisplus.test.testtargetwithin.Secure)")
    public void execute2(){
        System.out.println("@target(com.learn.annotation.Secure)");
    }

}

