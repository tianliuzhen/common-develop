package com.aaa.commondevelop.countDownLatch;

import com.aaa.commondevelop.domain.entity.User;
import com.aaa.commondevelop.service.User2Service;
import com.aaa.commondevelop.service.UserService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * @author liuzhen.tian
 * @version 1.0 TestCountDownLatch.java  2021/9/19 22:05
 */

@SpringBootTest
public class TestCountDownLatch {

    @Autowired
    private UserService userService;

    @Autowired
    private User2Service user2Service;

    @Test
    public void MergeResult() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);


        System.out.println("分页测试：：：");
        // LambdaQueryWrapper<User> allen = new QueryWrapper<User>().lambda().like(User::getName, "Allen");

        CountDownLatch countDownLatch = new CountDownLatch(10);

        // 注意：这里要用 list安全类
        // List<User> res = Lists.newArrayList();
        List<User> res = Collections.synchronizedList(Lists.newArrayList());
        Map<String, List<User>> resMap = Maps.newConcurrentMap();
        // CopyOnWriteArrayList res = new CopyOnWriteArrayList();

        List<JSONObject> jsonObjects = Lists.newArrayList();
        for (int i = 1; i <= 10; i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("current", i);
            jsonObject.put("size", 10);
            jsonObjects.add(jsonObject);
        }

        for (JSONObject jsonObject : jsonObjects) {
            executorService.submit(() -> {
                Page<User> page = new Page();
                System.out.println(Thread.currentThread().getId() + " = " + jsonObject.getInteger("current"));
                page.setSize(10).setCurrent(jsonObject.getInteger("current"));
                // Page<User> userPage = userService.page(page);
                IPage<User> userPage = user2Service.selectUserPage(page, "allen");
                System.out.println("================:" + userPage.getRecords().get(0).getId());
                resMap.put(UUID.randomUUID().toString(), userPage.getRecords());
                res.addAll(userPage.getRecords());
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();

        System.out.println("resMap.size() = " + resMap.size());
        System.out.println("res.size() = " + res.size());


    }
}
