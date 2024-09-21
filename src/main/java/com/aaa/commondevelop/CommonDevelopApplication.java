package com.aaa.commondevelop;

import com.aaa.commondevelop.web.TestMapper;
import com.baomidou.mybatisplus.core.override.MybatisMapperProxyFactory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @MapperScan 和 @Mapper 缺一不可
 */
@SpringBootApplication
// 原生的扫描
@org.mybatis.spring.annotation.MapperScan("com.aaa.commondevelop.mapper")
// tkMapper的扫描
@tk.mybatis.spring.annotation.MapperScan("com.aaa.commondevelop.mapper2")
@ImportResource(locations = {"classpath:spring-common.xml"})
// @EnableAsync(proxyTargetClass = true)
@EnableScheduling
// @EnableAspectJAutoProxy(exposeProxy = true)
@EnableTransactionManagement(order = 3)
public class CommonDevelopApplication {

    @Bean(name = "testMapper")
    public TestMapper testMapper(SqlSession sqlSession) {
        MybatisMapperProxyFactory<TestMapper> mybatisMapperProxyFactory = new MybatisMapperProxyFactory(TestMapper.class);

        return mybatisMapperProxyFactory.newInstance(sqlSession);
    }


    public static void main(String[] args) {
        SpringApplication.run(CommonDevelopApplication.class, args);
    }

}
