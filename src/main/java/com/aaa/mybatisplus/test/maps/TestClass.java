package com.aaa.mybatisplus.test.maps;

import java.util.concurrent.ConcurrentHashMap;

/**
 * description:
 * 如果将add方法前的synchronized去掉后输出结果为：9305，并不是想象中的10000。
 *
 * 如果只是调用put或者get方法，ConcurrentHashMap是线程安全的，但是如果调用了get后在调用map.put(key, value+1)
 * 之前有另外的线程去调用了put，然后你再去执行put，
 * 就有可能将结果覆盖掉，但这个其实也不能算ConcurrentHashMap线程不安全
 * ，ConcurrentHashMap内部操作是线程安全的，但是外部操作还是要靠自己来保证同步，即使在线程安全的情况下，也是可能违反原子操作规则。。。
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/20
 */
public class TestClass {
    private ConcurrentHashMap<String, Integer> map=new ConcurrentHashMap<String, Integer>();

    public synchronized void add(String key){
        Integer value=map.get(key);
        if(value==null){
            map.put(key, 1);
        } else {
            map.put(key, value + 1);
        }
        System.out.println(map.get(key));
    }
    public static void main(String[] args) {
        final TestClass t=new TestClass();
        for (int i = 0; i < 10000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    t.add("key");
                }
            }).start();
        }
    }
}
