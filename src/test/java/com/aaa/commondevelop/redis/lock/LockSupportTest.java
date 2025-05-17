package com.aaa.commondevelop.redis.lock;

import java.util.concurrent.locks.LockSupport;

/**
 * @author liuzhen.tian
 * @version 1.0 LockSupportTest.java  2025/5/17 15:28
 */
public class LockSupportTest {
    public static void main(String[] args) {
        System.out.println(1);
        LockSupport.park(); // 100微秒
        System.out.println(2);
    }
}
