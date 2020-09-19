package com.aaa.mybatisplus;

import com.aaa.mybatisplus.config.MybatisPlusConfig;
import com.aaa.mybatisplus.domain.entity.User;
import com.aaa.mybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

@SpringBootTest
class MybatisPlusApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
        System.out.println(("----- selectAll method test ------"));
        System.out.println(userMapper.getOne("1"));
    }

    @Test
    public void resolveAnnotation(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MybatisPlusConfig.class);
        String[] userControllers = context.getBeanDefinitionNames();
        for (String userController : userControllers) {
            System.out.println("userController = " + userController);
        }

    }


    // 传统xml
    @Test
    public void resolveXMl(){
        //1. 创建 Spring 的 IOC 容器
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");

        //2. 从 IOC 容器中获取 bean 的实例
//        HelloWorld  helloWorld = (HelloWorld) ctx.getBean("helloWorld");

    }
}
