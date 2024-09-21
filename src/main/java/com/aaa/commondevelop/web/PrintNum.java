package com.aaa.commondevelop.web;

/**
 * @author liuzhen.tian
 * @version 1.0 PrintNum.java  2020/12/21 23:06
 */
public class PrintNum implements Runnable {
    //是否现在先运行
    private boolean runNow;
    private Object lock;
    private int num;

    public PrintNum(boolean runNow, Object lock, int num) {
        this.runNow = runNow;
        this.lock = lock;
        this.num = num;
    }

    @Override
    public void run(){
        synchronized(lock){
            while(num <= 100) {
                if (runNow) {
                    runNow = false;
                } else {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print(num+" ");
                num += 2;
                lock.notify();
            }
        }
    }
}
