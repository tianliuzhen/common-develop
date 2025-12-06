package com.aaa.commondevelop.service.sqlDeadLock;

import com.aaa.commondevelop.domain.entity.Account;
import com.aaa.commondevelop.mapper.tk.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liuzhen.tian
 * @version 1.0 DeadLockTest.java  2025/12/6 20:55
 */
@Slf4j
@RunWith(SpringRunner.class)  // 👈 org.junit.Test 必须添加这个！
@SpringBootTest
public class DeadLockTest2 {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Test
    public void simpleGapLockDeadlock() throws InterruptedException {
        // 先清空表
        // accountMapper.deleteAll();
        accountMapper.deleteByExample(null);

        // 启动两个事务
        Thread t1 = new Thread(() -> {
            transactionTemplate.execute(status -> {
                try {
                    // 事务1：先查询一个范围
                    List<Account> accounts = accountMapper.selectRangeForUpdate(100, 200);

                    // 等待另一个事务启动
                    try { Thread.sleep(1000); } catch (Exception e) {}

                    // 在范围内插入
                    Account acc = new Account();
                    acc.setAccountNo("150");  // 在100~200之间
                    acc.setBalance(new BigDecimal("1000"));
                    accountMapper.insert(acc);
                    log.info("线程1成功");
                    return null;
                } catch (Exception e) {
                    log.error("线程1失败", e);
                    return null;
                }
            });
        });

        Thread t2 = new Thread(() -> {
            transactionTemplate.execute(status -> {
                try {
                    // 事务2：也查询相同范围
                    List<Account> accounts = accountMapper.selectRangeForUpdate(100, 200);

                    // 等待
                    try { Thread.sleep(600); } catch (Exception e) {}

                    // 在相同范围内插入
                    Account acc = new Account();
                    acc.setAccountNo("160");  // 也在100~200之间
                    acc.setBalance(new BigDecimal("1000"));
                    accountMapper.insert(acc);
                    log.info("线程2成功");
                    return null;
                } catch (Exception e) {
                    log.error("线程2失败", e);
                    return null;
                }
            });
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }


}
