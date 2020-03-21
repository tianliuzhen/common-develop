package com.aaa.mybatisplus.test.testtargetwithin;

import org.springframework.stereotype.Component;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/3/13
 */
@Component
@Secure
public class Leader extends Member{
    @Override
    public void say() {
        System.out.println("hello, members");
    }

    @Override
    public void who() {
        System.out.println("leader");
    }

    @Override
    public void doSomething() {
        System.out.println("leader doSomething");
    }

    public void self() {
        System.out.println("leader self");
    }
}

