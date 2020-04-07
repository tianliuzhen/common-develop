package com.aaa.mybatisplus.test.annotation;

import com.aaa.mybatisplus.entity.People;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/7
 */
@SpringBootTest
public class TestConfig {
    @Autowired
    private People people;

    @Autowired
    private People people1;
    @Test
    public void newInstance(){
        System.out.println("= = = = = = = = = = = = = ");
        System.out.println("people = " + people);
        System.out.println("= = = = = = = = = = = = = ");
        System.out.println("people = " + people1);
    }
}
