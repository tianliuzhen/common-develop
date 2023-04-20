package com.aaa.mybatisplus.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author liuzhen.tian
 * @version 1.0 BeanContextUtil.java  2022/7/22 22:27
 */
@Component
public class BeanContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanContextUtil.applicationContext = applicationContext;
    }

    public static <T> T getBean(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }

    public static <T> T getBean(Class<T> clz) {
        return (T) applicationContext.getBean(clz);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }


}
