package com.aaa.mybatisplus;

import com.aaa.mybatisplus.config.TestProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/11
 */
@SpringBootTest
public class TestYml {
    @Autowired
   private TestProperties test;
   @Test
    public void testYml(){
       System.out.println(test.getOldName());
   }

}
