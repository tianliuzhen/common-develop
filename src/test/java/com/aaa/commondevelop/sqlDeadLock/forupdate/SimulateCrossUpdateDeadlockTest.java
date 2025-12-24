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
import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liuzhen.tian
 * @version 1.0 SimulateCrossUpdateDeadlockTest.java  2025/12/6 20:55
 */
@Slf4j
@RunWith(SpringRunner.class)  // 👈 org.junit.Test 必须添加这个！
@SpringBootTest
public class SimulateCrossUpdateDeadlockTest {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 模拟交叉更新死锁
     * 事务1：A -> B 转账
     * 事务2：B -> A 转账
     */
    @Test
    public void simulateCrossUpdateDeadlock() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 线程1：从A向B转账
        Thread thread1 = new Thread(() -> {
            transactionTemplate.execute(status -> {
                try {
                    log.info("按AAA的顺序加锁.begin...");
                    // 按A->B的顺序加锁
                    Account accountA = accountMapper.selectForUpdateAccountNo("A001");

                    // 模拟业务处理耗时，增加死锁概率
                    Thread.sleep(100);

                    Account accountB = accountMapper.selectForUpdateAccountNo("A002");

                    // 更新账户
                    accountA.setBalance(accountA.getBalance().subtract(new BigDecimal("100")));
                    accountB.setBalance(accountB.getBalance().add(new BigDecimal("100")));

                    accountMapper.updateBalance(accountA);
                    accountMapper.updateBalance(accountB);

                    System.out.println("线程1转账完成");
                } catch (Exception e) {
                    log.error("线程1转账失败", e);
                }
                return null;
            });
        });

        // 线程2：从B向A转账
        Thread thread2 = new Thread(() -> {
            transactionTemplate.execute(status -> {
                try {
                    log.info("按BBB的顺序加锁.begin...");
                    // 按B->A的顺序加锁（与线程1顺序相反）
                    Account accountB = accountMapper.selectForUpdateAccountNo("A002");
                    // 模拟业务处理耗时
                    Thread.sleep(100);

                    Account accountA = accountMapper.selectForUpdateAccountNo("A001");

                    // 更新账户
                    accountB.setBalance(accountB.getBalance().subtract(new BigDecimal("100")));
                    accountA.setBalance(accountA.getBalance().add(new BigDecimal("100")));

                    accountMapper.updateBalance(accountB);
                    accountMapper.updateBalance(accountA);

                    System.out.println("线程2转账完成");
                } catch (Exception e) {
                    log.error("线程2转账失败", e);
                }
                return null;
            });
        });

        thread1.start();
        thread2.start();

        // 主线程等待子线程执行结束
        thread1.join();
        thread2.join();

        stopWatch.stop();
        // 大概0.3s 数据库会自动检测死锁，并且随机牺牲一个事务，回滚改事务解除死锁
        /*
         * org.springframework.dao.DeadlockLoserDataAccessException:
         * ### Error querying database.  Cause: com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException: Deadlock found when trying to get lock; try restarting transaction
         * ### The error may exist in com/aaa/commondevelop/mapper/tk/AccountMapper.java (best guess)
         * ### The error may involve com.aaa.commondevelop.mapper.tk.AccountMapper.selectForUpdate-Inline
         * ### The error occurred while setting parameters
         * ### SQL: SELECT * FROM account WHERE account_no = ? FOR UPDATE
         * ### Cause: com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException: Deadlock found when trying to get lock; try restarting transaction
         * ; Deadlock found when trying to get lock; try restarting transaction; nested exception is com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException: Deadlock found when trying to get lock; try restarting transaction
         */
        System.out.println("stopWatch.getTotalTimeSeconds() = " + stopWatch.getTotalTimeSeconds());

    }


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
