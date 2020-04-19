package com.aaa.mybatisplus;

import com.aaa.mybatisplus.config.MyProps;
import com.aaa.mybatisplus.config.TestProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

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

    @Autowired
    private MyProps myProps;

    @Test
    public void testYml(){
       System.out.println(test.getOldName());
   }

    @Test
    public void testYmlV2(){
        Map<String, String> maps = myProps.getMaps();
        maps.forEach((k,v)->{
            System.out.println("k = " + k);
            System.out.println("v = " + v);
        });
        List<String> list = myProps.getList();
        for (String s : list) {
            System.out.println("s = " + s);
        }
        Map<String, List<String>> varmaplist = myProps.getVarmaplist();
        for (Map.Entry<String, List<String>> stringListEntry : varmaplist.entrySet()) {
            System.out.println("stringListEntry.getKey() = " + stringListEntry.getKey());
            System.out.println("stringListEntry.getValue() = " + stringListEntry.getValue());
        }
    }

}
