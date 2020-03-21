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
public class Member extends User{

    @Override
    public void who() {
        System.out.println("member");
    }

    public void doSomething() {
        System.out.println("member doSomething");
    }

    public void getCompany() {
        System.out.println("zero tec");
    }
}

