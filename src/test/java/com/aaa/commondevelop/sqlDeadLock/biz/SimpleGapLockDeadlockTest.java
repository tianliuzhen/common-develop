package com.aaa.commondevelop.sqlDeadLock.biz;

/**
 * @author liuzhen.tian
 * @version 1.0 SimpleGapLockDeadlockTest.java  2025/12/7 19:46
 */

import com.aaa.commondevelop.domain.entity.Account;
import com.aaa.commondevelop.mapper.tk.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 在 REPEATABLE READ 隔离级别下，使用唯一索引查询不存在的记录时，会获取间隙锁
 */
@Slf4j
@SpringBootTest
public class SimpleGapLockDeadlockTest {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 验证：在特定条件下，普通 SELECT 也可能获取锁
     */
    @Test
    public void simulatePotentialGapLockScenario() throws InterruptedException {
        // 准备数据：account_no 是唯一索引
        accountMapper.deleteByExample(null);
        accountMapper.insert(new Account("A0010", new BigDecimal("1000")));
        accountMapper.insert(new Account("A0030", new BigDecimal("1000")));
        accountMapper.insert(new Account("A0050", new BigDecimal("1000")));

        CountDownLatch latch = new CountDownLatch(2);
        AtomicInteger deadlockCount = new AtomicInteger(0);

        Thread thread1 = new Thread(() -> {
            try {
                transactionTemplate.execute(status -> {
                    try {
                        log.info("线程1开始事务");

                        log.info("account_no='A0020'不存在，位于(A0010,A0030)间隙,事务1获得间隙锁：(A0010,A0030)");
                        accountMapper.updateBalanceByAccount("A0020", new BigDecimal("100"));

                        latch.countDown();
                        latch.await();
                        Thread.sleep(100);

                        // 尝试插入记录
                        log.info("线程1插入 A0040");
                        accountMapper.insert(new Account("A0040", new BigDecimal("4000")));

                        log.info("线程1操作成功");

                        Thread.sleep(1000 * 5);
                    } catch (Exception e) {
                        handleDeadlockException(e, "线程1", deadlockCount);
                        status.setRollbackOnly();
                    }
                    return null;
                });
            } catch (Exception e) {
                log.error("线程1异常", e);
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                transactionTemplate.execute(status -> {
                    try {
                        log.info("线程2开始事务");

                        log.info("account_no='A0040'不存在，位于(A0030,A0050)间隙");
                        accountMapper.updateBalanceByAccount("A0040", new BigDecimal("100"));

                        latch.countDown();
                        latch.await();
                        Thread.sleep(100);

                        // 尝试插入记录
                        log.info("线程2插入 A0020");
                        accountMapper.insert(new Account("A0020", new BigDecimal("2000")));

                        log.info("线程2操作成功");

                        Thread.sleep(1000 * 5);
                    } catch (Exception e) {
                        handleDeadlockException(e, "线程2", deadlockCount);
                        status.setRollbackOnly();
                    }
                    return null;
                });
            } catch (Exception e) {
                log.error("线程2异常", e);
            }
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        log.info("测试完成，死锁发生次数: {}", deadlockCount.get());
    }

    private void handleDeadlockException(Exception e, String threadName, AtomicInteger deadlockCount) {
        log.error("{} 发生异常: {}", threadName, e.getMessage(), e);

        Throwable cause = e;
        while (cause != null) {
            if (cause instanceof SQLException) {
                SQLException sqlEx = (SQLException) cause;
                if (sqlEx.getErrorCode() == 1213 || sqlEx.getMessage().contains("Deadlock")) {
                    log.info("{} 遇到死锁！", threadName);
                    deadlockCount.incrementAndGet();
                    return;
                }
            }
            cause = cause.getCause();
        }
        log.info("{} 遇到其他异常: {}", threadName, e.getMessage());
    }
}
