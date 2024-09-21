package com.aaa.commondevelop;

import com.aaa.commondevelop.web.PrintNum;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author liuzhen.tian
 * @version 1.0 PrintNum.java  2020/12/21 23:05
 */
@SpringBootTest
public class PrintNumTest  {
    @Test
    public void print(){
        Object lock = new Object();
        Thread t1 = new Thread(new PrintNum(true, lock, 1));
        Thread t2 = new Thread(new PrintNum(false, lock, 2));
        //t2先运行，先进入等待状态
        t2.start();
        //t1直接运行，唤醒t2之后进入等待
        t1.start();
    }
}


