package com.aaa.commondevelop.util;

/**
 * @author liuzhen.tian
 * @version 1.0 ThreadPoolStallExample.java  2024/7/26 21:49
 */
import java.util.concurrent.*;

public class ThreadPoolStallExample {

    private static final ExecutorService executor = Executors.newFixedThreadPool(2); // 假设核心线程数为2

    // 模拟父任务
    static class ParentTask implements Runnable {
        private final String name;
        private final CountDownLatch latch; // 用于等待子任务完成

        public ParentTask(String name, CountDownLatch latch) {
            this.name = name;
            this.latch = latch;
        }

        @Override
        public void run() {
            System.out.println(name + " 开始执行");

            // 提交第一个子任务
            executor.submit(() -> {
                System.out.println(name + " 的第一个子任务执行完成");
                // 假设这里没有其他同步或等待操作
            });

            // 提交第二个子任务，但实际上这个不会被立即执行，因为线程池已满
            executor.submit(() -> {
                try {
                    // 模拟耗时操作
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println(name + " 的第二个子任务执行完成");
                latch.countDown(); // 通知父任务子任务已完成（但在这个例子中，这并不会实际改变什么）
            });

            // 父任务“等待”子任务完成（这里使用CountDownLatch来模拟）
            try {
                latch.await(); // 实际上，这里并不真正需要等待，只是为了演示
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println(name + " 完成");
        }
    }

    public static void main(String[] args) throws Exception {
        CountDownLatch latch1 = new CountDownLatch(1);
        CountDownLatch latch2 = new CountDownLatch(1);

        // 提交两个父任务
        executor.submit(new ParentTask("父任务1", latch1));
        executor.submit(new ParentTask("父任务2", latch2));

        // 等待父任务完成（这里只是为了演示，实际上父任务可能因为等待子任务而永远不会完成）
        latch1.await();
        latch2.await();

        executor.shutdown();
        // 注意：由于线程池中的任务可能永远不会完成（因为没有足够的线程来执行它们），
        // 这里可能需要调用shutdownNow()并处理中断，或者设置一个超时来等待线程池关闭。
    }
}
