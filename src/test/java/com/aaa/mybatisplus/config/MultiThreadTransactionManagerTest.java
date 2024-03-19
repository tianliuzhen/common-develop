package com.aaa.mybatisplus.config;

import com.aaa.mybatisplus.domain.entity.User;
import com.aaa.mybatisplus.mapper.UserMapper;
import com.aaa.mybatisplus.service.UserService;
import com.aaa.mybatisplus.util.ThreadPoolUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author liuzhen.tian
 * @version 1.0 MultiThreadTransactionManagerTest.java  2024/3/19 21:40
 */

@SpringBootTest
public class MultiThreadTransactionManagerTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    /**
     * 借鉴：https://blog.csdn.net/can_meng_yun/article/details/128587079
     */
    @Test
    public void testMultiThread() {
        MultiThreadTransactionManager multiThreadTransactionManager =
                new MultiThreadTransactionManager(platformTransactionManager, 20, TimeUnit.SECONDS);

        List<Runnable> runnableList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            runnableList.add(() -> {
                User user = new User();
                user.setId(UUID.randomUUID().toString());
                user.setName("tom");
                user.setOnlineTime(LocalDateTime.now());
                user.setCreateTime(LocalDateTime.now());
                user.setUpdateTime(new Date());
                user.setStatus(0);
                user.setIsDel(0);
                userMapper.insertUser(user);
            });
        }

        /**
         * 有俩个严重问题
         *
         * 1、要注意，要尽可能的把核心线程数设置为 大于 异步线程的数，
         *  否则会出现阻塞的问题，这个问题的核心是，每个线程任务因为await都会进入阻塞无法释放，
         *  因为二阶段提交时，需要等待所有的任务执行完成，而如果出现了任务数大于核心线程数，
         *  就会出现死锁（二阶段等一阶段，一阶段再等二阶段），直至达到默认超时时间，自动释放二阶段的锁，一阶段才能继续执行
         *
         * 2、主线程仍然存在主线程，发生异常，子线程无非回滚的问题 todo
         */
        multiThreadTransactionManager.execute(runnableList, ThreadPoolUtil.common_pool);
    }

    /**
     * 参考：https://www.cnblogs.com/fix200/p/18066537 并进行进行优化
     */
    @Test
    public void testMultiThreadV2() {
        MultiThreadTransactionManagerV2 multiThreadTransactionManager =
                new MultiThreadTransactionManagerV2(platformTransactionManager, 20, TimeUnit.SECONDS);

        List<Runnable> runnableList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            runnableList.add(() -> {
                User user = new User();
                user.setId(UUID.randomUUID().toString());
                user.setName("tom");
                user.setOnlineTime(LocalDateTime.now());
                user.setCreateTime(LocalDateTime.now());
                user.setUpdateTime(new Date());
                user.setStatus(0);
                user.setIsDel(0);
                userMapper.insertUser(user);
            });
        }

        multiThreadTransactionManager.execute(runnableList, ThreadPoolUtil.common_pool);
    }
}
