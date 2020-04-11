package com.aaa.mybatisplus.test.annotation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/7
 */
@SpringBootTest
public class TestHunter {
    @Autowired
    @Qualifier("hunter1")
    Hunter  hunter;

    public void show(){
        hunter.display();
    }
    @Test
    public void test(){
        show();
    }

}
