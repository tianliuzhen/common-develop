package com.aaa.mybatisplus.test.testtargetwithin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/3/13
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
    private Member member;
    @Autowired
    private Leader leader;

    // 实现
    @Test
    public void test1() {
        System.out.println("---------------member---------------");
        member.who();
        System.out.println("---------------leader---------------");
        leader.who();
    }

    @Test
    public void test2() {
        // 继承
        System.out.println("---------------member---------------");
        member.say();
        // 重载
        System.out.println("---------------leader---------------");
        leader.say();
    }

    @Test
    public void test3() {
        System.out.println("---------------member---------------");
        member.root();
        System.out.println("---------------leader---------------");
        leader.root();
    }

    @Test
    public void test4() {
        // 独有方法
        System.out.println("---------------member---------------");
        member.doSomething();
        // 子类重写
        System.out.println("---------------leader---------------");
        leader.doSomething();
    }

    @Test
    public void test5() {
        System.out.println("---------------member---------------");
        member.getCompany();
        System.out.println("---------------leader---------------");
        leader.getCompany();
    }

    // 独有的方法
    @Test
    public void test6() {
        System.out.println("---------------leader---------------");
        leader.self();
    }
}
