package com.aaa.mybatisplus;

import com.aaa.mybatisplus.entity.User;
import com.aaa.mybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

}
