package com.aaa.commondevelop.sqlDeadLock.forupdate;

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
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liuzhen.tian
 * @version 1.0 SimulateCrossUpdateDeadlockTest.java  2025/12/6 20:55
 */
@Slf4j
@RunWith(SpringRunner.class)  // 👈 org.junit.Test 必须添加这个！
@SpringBootTest
public class SimulateRealGapLockDeadlockTest {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Next-Key Lock
     * 注意：需要非唯一索引！如果你的 account_no 是唯一索引或主键：则无法实现间隙锁的复现
     * ✅ 隔离级别：必须是 REPEATABLE READ
     * ✅ 索引类型：必须使用非唯一索引字段
     * ✅ 锁定记录存在：SELECT FOR UPDATE 的记录必须存在
     * ✅ 插入范围：插入的值必须在间隙中
     * ✅ 事务未提交：两个事务都保持打开状态
     *
     * @throws InterruptedException
     */
    @Test
    public void simulateRealGapLockDeadlock() throws InterruptedException {
        // 设置事务隔离级别为 REPEATABLE READ（这是默认的，但最好明确）
        // transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);

        // 准备数据：创建一个明显的间隙
        prepareGapData();

        CountDownLatch latch = new CountDownLatch(2);
        AtomicInteger deadlockCount = new AtomicInteger(0);

        Thread thread1 = new Thread(() -> {
            try {
                transactionTemplate.execute(status -> {
                    try {
                        log.info("线程1开始，事务ID: {}", getCurrentTransactionId());

                        // ⚠️ 关键：对存在的记录加锁，获取间隙锁
                        // 锁定 A002，这会锁定 (A001, A002] 和 (A002, A005)
                        accountMapper.selectForUpdateAccountNo("A002");

                        // 等待另一个事务也获得锁
                        latch.countDown();
                        latch.await();
                        log.info("线程1尝试插入 A003（在间隙中）");
                        // 尝试在 A002 和 A005 之间插入
                        Account newAccount = new Account();
                        newAccount.setAccountNo("A003");  // 在 A002~A005 间隙中
                        newAccount.setBalance(new BigDecimal("500"));
                        accountMapper.insert(newAccount);

                        log.info("线程1插入成功");
                    } catch (Exception e) {
                        log.error("<UNK>线程1<UNK>: " + e.getMessage());
                        handleDeadlockException(e, "线程1", deadlockCount);
                        status.setRollbackOnly();
                    }
                    return null;
                });
            } catch (Exception e) {
                log.info("线程1外层异常: " + e.getMessage());
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                transactionTemplate.execute(status -> {
                    try {
                        log.info("线程2开始，事务ID: " + getCurrentTransactionId());

                        // ⚠️ 关键：对同一个范围的另一侧加锁
                        // 锁定 A005，这会锁定 (A002, A005] 和 (A005, A008)
                        accountMapper.selectForUpdateAccountNo("A005");

                        // 等待另一个事务也获得锁
                        latch.countDown();
                        latch.await();
                        log.info("线程2尝试插入 A004（在间隙中）");
                        // 尝试在 A002 和 A005 之间插入（与线程1相同的间隙！）
                        Account newAccount = new Account();
                        newAccount.setAccountNo("A004");  // 也在 A002~A005 间隙中
                        newAccount.setBalance(new BigDecimal("500"));
                        accountMapper.insert(newAccount);

                        log.info("线程2插入成功");
                    } catch (Exception e) {
                        log.error("<UNK>线程2<UNK>: " + e.getMessage());
                        handleDeadlockException(e, "线程2", deadlockCount);
                        status.setRollbackOnly();
                    }
                    return null;
                });
            } catch (Exception e) {
                log.info("线程2外层异常: " + e.getMessage());
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        log.info("死锁发生次数: " + deadlockCount.get());
    }

    private void prepareGapData() {
        // 清空并创建特定的数据间隙
        accountMapper.deleteByExample(null);

        // 插入数据，制造明显的间隙
        List<Account> accounts = Arrays.asList(
                createAccount("A001", "1000"),
                createAccount("A002", "1000"),
                createAccount("A005", "1000"),  // A002 和 A005 之间有间隙
                createAccount("A008", "1000")
        );

        accounts.forEach(accountMapper::insert);
    }

    private Account createAccount(String accountNo, String balance) {
        Account account = new Account();
        account.setAccountNo(accountNo);
        account.setBalance(new BigDecimal(balance));
        return account;
    }

    private void handleDeadlockException(Exception e, String threadName, AtomicInteger deadlockCount) {
        Throwable cause = e.getCause();
        if (cause instanceof SQLException) {
            SQLException sqlEx = (SQLException) cause;
            // MySQL 死锁错误码是 1213
            if (sqlEx.getErrorCode() == 1213 || sqlEx.getMessage().contains("Deadlock")) {
                log.info(threadName + " 遇到死锁: " + sqlEx.getMessage());
                deadlockCount.incrementAndGet();
            } else {
                log.info(threadName + " SQL异常: " + sqlEx.getMessage());
            }
        } else {
            log.info(threadName + " 异常: " + e.getMessage());
        }
    }

    // 获取当前事务ID（用于调试）
    private String getCurrentTransactionId() {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT TRX_ID FROM INFORMATION_SCHEMA.INNODB_TRX WHERE TRX_MYSQL_THREAD_ID = CONNECTION_ID()",
                    String.class
            );
        } catch (Exception e) {
            return "unknown";
        }
    }
}
