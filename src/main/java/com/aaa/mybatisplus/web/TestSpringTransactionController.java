package com.aaa.mybatisplus.web;

import com.aaa.mybatisplus.config.MultiThreadTransactionManagerV2;
import com.aaa.mybatisplus.config.global.exceptions.BizException;
import com.aaa.mybatisplus.domain.entity.User;
import com.aaa.mybatisplus.mapper.UserMapper;
import com.aaa.mybatisplus.util.ThreadPoolUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author liuzhen.tian
 * @version 1.0 TestSpringTransactionController.java  2022/8/9 21:41
 */
@Slf4j
@RestController
@RequestMapping("/testSpringTransaction")
public class TestSpringTransactionController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;
    @Autowired
    private TransactionDefinition transactionDefinition;

    /**
     * 编程式事务：PlatformTransactionManager
     */
    @GetMapping("/platformTransactionManager")
    public void platformTransactionManager() {
        TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);
        try {
            insertUser();
            platformTransactionManager.commit(transaction);
            // 模拟异常
            int a = 1 / 0;
        } catch (Exception e) {
            e.printStackTrace();
            platformTransactionManager.rollback(transaction);
        }
    }

    @GetMapping("/transactionTemplate")
    public void transactionTemplate() {
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            insertUser();
            // 模拟异常
            try {
                int a = 1 / 0;
            } catch (Exception e) {
                throw e;
            }
        });
    }

    @GetMapping("/transactionTemplate2")
    public void transactionTemplate2() {
        transactionTemplate.executeWithoutResult(transactionStatus -> {

            insertUser();

            // 模拟异常
            try {
                int a = 1 / 0;
            } catch (Exception e) {
                transactionStatus.setRollbackOnly();
            }
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @GetMapping("/transactionTemplate3")
    public void transactionTemplate3() {

        insertUser();

        // 模拟异常
        try {
            int a = 1 / 0;
        } catch (Exception e) {
            // throw e;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    public void insertUser() {
        User user = new User();
        user.setId("6");
        user.setName("tom");
        user.setStatus(0);
        user.setIsDel(0);
        userMapper.insertUser(user);
    }


    /**
     * 内部调用：事务不生效
     */
    @GetMapping(value = "/updateUserByTrans")
    public void updateUserByTrans() {
        doUpdateUser();
    }

    @Autowired
    private TestSpringTransactionController testSpringTransactionController;

    /**
     * 内部调用：事务不生效解决方案 1：自己注入自己
     */
    @GetMapping(value = "/updateUserByTrans2")
    public void updateUserByTrans2() {
        // 重新注入自己去调用自己的内部方法
        testSpringTransactionController.doUpdateUser();
    }

    /**
     * 内部调用：事务不生效解决方案 2：代理类调用
     */
    @GetMapping(value = "/updateUserByTrans3")
    public void updateUserByTrans3() {
        // 用代理类去调用
        TestSpringTransactionController proxy = (TestSpringTransactionController) AopContext.currentProxy();
        proxy.doUpdateUser();
    }


    @Transactional(rollbackFor = Exception.class)
    public void doUpdateUser() {
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("id", "4");
        hashMap.put("name", "3");
        userMapper.updateUserByCondition(hashMap);
        throw new BizException();
    }


    /**
     * 异步线程的带事务执行的正确方案
     */
    @GetMapping("/asyncTrans")
    public void asyncTrans() {
        MultiThreadTransactionManagerV2 multiThreadTransactionManager =
                new MultiThreadTransactionManagerV2(platformTransactionManager, 20, TimeUnit.SECONDS);
        multiThreadTransactionManager.execute(Lists.newArrayList(() -> {
            insertUser();
            // 模拟异常
            try {
                int a = 1 / 0;
            } catch (Exception e) {
                // throw e;
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }), ThreadPoolUtil.common_pool);
    }
}
