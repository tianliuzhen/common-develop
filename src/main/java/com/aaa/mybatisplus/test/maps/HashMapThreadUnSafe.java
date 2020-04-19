package com.aaa.mybatisplus.test.maps;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/19
 */
public class HashMapThreadUnSafe {
    public static void main(String[] args) throws InterruptedException {
        Map map = new ConcurrentHashMap(100*100);
        for (int i = 0; i < 100; i++) {
            TestThread testThread = new TestThread(map);
            testThread.start();
            testThread.join();
        }
        System.out.println("hashMap的最终长度"+map.size());
    }

}
