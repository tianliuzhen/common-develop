package com.aaa.mybatisplus.retry;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

/**
 * @author liuzhen.tian
 * @version 1.0 RetryApplication.java  2021/2/19 23:36
 */
@Configuration
@EnableRetry  //注意需要该注解方可生效
public class RetryApplication {
    /**
     * 参考：
     *      Spring Retry重试机制  https://www.jianshu.com/p/cc7abf831900
     *      Spring 中的重试机制，简单、实用！https://mp.weixin.qq.com/s/bu4JIWUH5OlokH4IS3pK6g
     */

    //直接测试代码
    public static void main(String[] args) throws Exception{
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext("com.aaa.mybatisplus.retry");
        PayService service1 = applicationContext.getBean("payService", PayService.class);
        service1.minGoodsNum(-1);
    }
}
