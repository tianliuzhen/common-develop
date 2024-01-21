package com.aaa.mybatisplus.config.intercept;

import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.context.annotation.Bean;

/**
 * @author liuzhen.tian
 * @version 1.0 BeanNameAutoProxyCreatorCOnfig.java  2023/4/20 22:45
 */
// @Configuration
public class BeanNameAutoProxyCreatorConfig {

    @Bean
    public MyMethodInterceptor myMethodInterceptor(){
        return new MyMethodInterceptor();
    }

    //使用BeanNameAutoProxyCreator来创建代理
    @Bean
    public BeanNameAutoProxyCreator beanNameAutoProxyCreator(){
        BeanNameAutoProxyCreator beanNameAutoProxyCreator=new BeanNameAutoProxyCreator();

        /**
         * 注意：所有的spring bean默认都是首字母小写，且指的不是接口，是具体的类
         * user2Service* ：表示可以拦截 user2Service* 开头的类
         * *ServiceImpl  ：表示可以拦截 *ServiceImpl  结尾的类
         */
        // 设置要创建代理的那些Bean的名字，如果设置多个，每个的关系是 or
        beanNameAutoProxyCreator.setBeanNames("user2Service*","*ServiceImpl","userMapper");
        // 设置拦截链名字(这些拦截器是有先后顺序的)
        beanNameAutoProxyCreator.setInterceptorNames("myMethodInterceptor");
        // 属性proxyTargetClass 为true表示 拦截的对象为一个类
        beanNameAutoProxyCreator.setProxyTargetClass(true);
        return beanNameAutoProxyCreator;
    }




    // private final String PROPAGATION_NAME_CHANGE = "PROPAGATION_REQUIRED,-Exception";
    // private final String PROPAGATION_NAME_SELECT = "PROPAGATION_REQUIRED,-Exception,readOnly";

    // 传统spring配置事务拦截器
    // @Autowired
    // private DataSourceTransactionManager transactionManager;

    // @Bean(name = "transactionManagerAdvice")
    // public TransactionInterceptor transactionInterceptor() throws Exception {
    //     Properties properties = new Properties();
    //     properties.setProperty("add*", PROPAGATION_NAME_CHANGE);
    //     properties.setProperty("save*", PROPAGATION_NAME_CHANGE);
    //     properties.setProperty("insert*", PROPAGATION_NAME_CHANGE);
    //     properties.setProperty("update*", PROPAGATION_NAME_CHANGE);
    //     properties.setProperty("delete*", PROPAGATION_NAME_CHANGE);
    //     properties.setProperty("get*", PROPAGATION_NAME_SELECT);
    //     properties.setProperty("find*", PROPAGATION_NAME_SELECT);
    //     TransactionInterceptor tsi = new TransactionInterceptor(transactionManager, properties);
    //     return tsi;
    // }

    //
    // @Bean
    // public BeanNameAutoProxyCreator txProxy() {
    //     BeanNameAutoProxyCreator beanNameAutoProxyCreator = new BeanNameAutoProxyCreator();
    //     beanNameAutoProxyCreator.setInterceptorNames("transactionManagerAdvice");
    //     beanNameAutoProxyCreator.setBeanNames("*Service", "*ServiceImpl");
    //     beanNameAutoProxyCreator.setProxyTargetClass(true);
    //     return beanNameAutoProxyCreator;
    // }
}
