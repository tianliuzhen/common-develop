package com.aaa.mybatisplus.threads;

import com.aaa.mybatisplus.config.SnowflakeIdWorker;

/**
 * description: 测试雪花算法
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2019-12-20
 */
public class TestThread {
    public static void main(String[] args) {
        /**
         * 控制台输出结果：
         * 主线程ID是： 1
         * 名称线程2的线程ID是：1
         * 名称线程1的线程ID是：11
         * 结论：
         * 1、主线程和线程2的线程ID相同，说明直接调用run()方法不会创建新的线程，而是在主线程中直接调用run()方法，普通的方法调用
         * 2、线程1先调用start()方法，而后线程2调用run()方法，最终却线程2先于线程1输出，说明新建的线程并不会影响主线程的执行顺序
         */
        System.out.println("主线程ID是： " + Thread.currentThread().getId());
        Thread t1 = new MyThread("线程1");
        t1.start();
        Thread t2 = new MyThread("线程2");
        /*直接调用run()方法*/
        t2.run();
    }
}
/**
 * 自定义线程
 */
class MyThread extends Thread{
    /*线程名称*/
    private String name;
    public MyThread(String name) {
        this.name = name;
    }
    @Override
    public void run() {
        long uid = new SnowflakeIdWorker(5, 6).nextId();
        System.out.println(uid);
        System.out.println("名称" + name + "的线程ID是：" + Thread.currentThread().getId());
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
