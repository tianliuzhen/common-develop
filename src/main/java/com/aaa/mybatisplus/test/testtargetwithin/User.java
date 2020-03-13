package com.aaa.mybatisplus.test.testtargetwithin;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/3/13
 */
public abstract class User {
    public abstract void who();

    public void say() {
        System.out.println("hello");
    }

    public void root() {
        System.out.println("user");
    }
}
