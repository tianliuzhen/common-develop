package com.aaa.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *  @MapperScan 和 @Mapper 缺一不可
 */
@SpringBootApplication
@MapperScan("com.aaa.mybatisplus.mapper")
@EnableAsync
@EnableScheduling
public class MybatisPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusApplication.class, args);
    }

}
