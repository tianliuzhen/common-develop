package com.aaa.commondevelop.redis;

import java.util.concurrent.*;

/**
 * 可设置 redis 最大执行时间，实现方法超时回滚。
 * @author liuzhen.tian
 * @version 1.0 test.java  2020/12/18 17:59
 */
public class TestTimeOut {
    public static void main(String[] args) {
        Executor executor= Executors.newSingleThreadExecutor();
        FutureTask<String> future=new FutureTask<String>(new Callable<String>() {
            public String call() throws Exception {
                // TODO Auto-generated method stub
                TestTimeOut m=new TestTimeOut();
                return m.getValue();
            }
        });
        executor.execute(future);
        try{
            String result=future.get(1, TimeUnit.SECONDS);
            System.out.println(result);
        }catch (InterruptedException e) {
            // TODO: handle exception
            System.out.println("方法执行中断");
            // future.cancel(true);
        }catch (ExecutionException e) {
            System.out.println("Excution异常");
            // TODO: handle exception
            future.cancel(true);
        }catch (TimeoutException e) {
            // TODO: handle exception
            System.out.println("方法执行时间超时");
            //future.cancel(true);
        }
        System.out.println("爱上大声地");
    }
    public String getValue(){
        try{
            Thread.sleep(2000);
        }catch (Exception e) {

            e.printStackTrace();// TODO: handle exception
        }
        return "阿斯顿撒旦阿斯顿 sad";
    }
}
