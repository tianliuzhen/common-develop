package com.aaa.mybatisplus.web;

import com.aaa.mybatisplus.domain.entity.User;
import com.aaa.mybatisplus.domain.enums.GenderEnum;
import com.aaa.mybatisplus.mapper.UserMapper;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @author liuzhen.tian
 * @version 1.0 TestCrudBatchController.java  2022/4/24 20:56
 */

@RestController
@RequestMapping(value = "batch")
public class TestCrudBatchController {
    @Autowired
    private UserMapper userMapper;

    /**
     * 批量更新：基于注解
     */
    @PostMapping("/batchUpdateUser")
    public void batchUpdateUser() {
        ArrayList<User> users = Lists.newArrayList(
                new User("1", "123@qq.com"),
                new User("2", "456@qq.com"));
        Integer res = userMapper.batchUpdateUser(users);
        System.out.println("res = " + res);
    }

    /**
     * 批量更新：基于xml （普通for）
     */
    @PostMapping("/batchUpdateUser2")
    public void batchUpdateUser2() {
        User user = new User("1", "123@qq.com");
        user.setAge(11L);
        ArrayList<User> users = Lists.newArrayList(
                user,
                new User("2", "456@qq.com"));
        Integer res = userMapper.batchUpdateUser2(users);
        System.out.println("res = " + res);
    }

    /**
     * 批量更新：基于xml （case when）
     */
    @PostMapping("/batchUpdateUser3")
    public void batchUpdateUser3() {
        ArrayList<User> users = Lists.newArrayList(
                new User("1", "123@qq.com"),
                new User("2", "456@qq.com"));
        Integer res = userMapper.batchUpdateUser3(users);
        System.out.println("res = " + res);
    }

    /**
     * 批量新增：基于xml （for）
     */
    @PostMapping("/batchAddUser")
    public void batchAddUser() {
        ArrayList<User> users = Lists.newArrayList(
                new User("3", "aaa", GenderEnum.FEMALE, 11L, "123@qq.com", 0, 0, 1, LocalDateTime.now()),
                new User("4", "bbb", GenderEnum.FEMALE, 12L, "123@qq.com", 0, 0, 1, LocalDateTime.now()));
        Integer res = userMapper.batchAddUser(users);
        System.out.println("res = " + res);
    }

    /**
     * 批量新增：基于xml （for）
     */
    @PostMapping("/batchAddUser2")
    public void batchAddUser2() {
        ArrayList<User> users = Lists.newArrayList(
                new User("3", "aaa", GenderEnum.FEMALE, 11L, "123@qq.com", 0, 0, 1, LocalDateTime.now()),
                new User("4", "bbb", GenderEnum.FEMALE, 12L, "123@qq.com", 0, 0, 1, LocalDateTime.now()));
        Integer res = userMapper.batchAddUser2(users);
        System.out.println("res = " + res);
    }
}
