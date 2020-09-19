package com.aaa.mybatisplus.util;

import com.aaa.mybatisplus.config.snowflakeId.SnowflakeComponent;
import com.aaa.mybatisplus.config.snowflakeId.SnowflakeIdWorker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author liuzhen.tian
 * @version 1.0 SnowflakeIdWorkerTest.java  2020/9/16 11:34
 */
@SpringBootTest
public class SnowflakeIdWorkerTest {
    @Autowired
    private SnowflakeComponent snowflakeComponent;
    @Test
    public void  test(){
        SnowflakeIdWorker instance = snowflakeComponent.getInstance();
        for (int i = 0; i < 10; i++) {
            System.out.println("instance.nextId() = " + instance.nextId());
        }
    }

}
