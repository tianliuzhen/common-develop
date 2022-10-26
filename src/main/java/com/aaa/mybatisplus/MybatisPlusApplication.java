package com.aaa.mybatisplus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @MapperScan 和 @Mapper 缺一不可
 */
@SpringBootApplication
// 原生的扫描
@org.mybatis.spring.annotation.MapperScan("com.aaa.mybatisplus.mapper")
// tkMapper的扫描
// @tk.mybatis.spring.annotation.MapperScan("com.aaa.mybatisplus.mapper2")
@ImportResource(locations={"classpath:spring-common.xml"})
@EnableAsync
@EnableScheduling
public class MybatisPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusApplication.class, args);
    }

}
