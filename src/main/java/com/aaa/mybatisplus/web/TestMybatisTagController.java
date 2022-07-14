package com.aaa.mybatisplus.web;

import com.aaa.mybatisplus.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author liuzhen.tian
 * @version 1.0 TestMybatisTagController.java  2022/7/14 21:10
 */
@Slf4j
@RestController
@RequestMapping("/testMybatisTag")
public class TestMybatisTagController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/updateUserByCondition")
    public void updateUserByCondition() {
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("id", "4");
        hashMap.put("name", "");
        hashMap.put("email", "411@qq.com");
        hashMap.put("age", 0L);
        userMapper.updateUserByCondition(hashMap);
    }
}


