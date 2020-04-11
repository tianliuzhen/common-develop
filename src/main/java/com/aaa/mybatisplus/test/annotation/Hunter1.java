package com.aaa.mybatisplus.test.annotation;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/7
 */
@Component
public class Hunter1 implements Hunter {
    @Override
    public void display() {
        System.out.println("hunter1");
    }
}
