package com.aaa.commondevelop.sqlDeadLock.biz;

import com.aaa.commondevelop.domain.entity.Account;
import com.aaa.commondevelop.domain.request.TransferRequest;
import com.aaa.commondevelop.mapper.tk.AccountMapper;
import com.aaa.commondevelop.service.impl.TransferService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liuzhen.tian
 * @version 1.0 SimulateCrossUpdateDeadlock.java  2025/12/7 19:20
 */
@SpringBootTest
@Slf4j
public class SimulateCrossUpdateDeadlock {

    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountMapper accountMapper;

    /**
     * 真实业务死锁场景：用户间互相转账
     *
     * 模拟两个并发转账：
     * 1. 用户A -> 用户B 转100元
     * 2. 用户B -> 用户A 转50元
     *
     * 死锁发生过程：
     * 时间线：
     * T1: 事务1查询A账户，事务2查询B账户
     * T2: 事务1更新A账户（扣款），获得A的锁
     * T3: 事务2更新B账户（扣款），获得B的锁
     * T4: 事务1尝试更新B账户（收款）→ 等待事务2释放B的锁
     * T5: 事务2尝试更新A账户（收款）→ 等待事务1释放A的锁
     * T6: 🚨 死锁！MySQL检测到死锁，回滚其中一个事务
     */
    @Test
    public void testRealTransferDeadlock() throws InterruptedException {
        // 初始化测试数据
        initTestData();

        CountDownLatch latch = new CountDownLatch(2);
        AtomicInteger deadlockCount = new AtomicInteger(0);

        // 线程1：A -> B 转100元
        Thread thread1 = new Thread(() -> {
            try {
                TransferRequest request = new TransferRequest();
                request.setFromAccountNo("A001");
                request.setToAccountNo("A002");
                request.setAmount(new BigDecimal("100"));

                log.info("线程1开始：A001 -> A002 转100元");
                transferService.transfer(request);
                log.info("线程1转账成功");
            } catch (Exception e) {
                log.error("线程1转账失败: {}", e.getMessage());
                checkDeadlock(e, "线程1", deadlockCount);
            } finally {
                latch.countDown();
            }
        });

        // 线程2：B -> A 转50元
        Thread thread2 = new Thread(() -> {
            try {
                TransferRequest request = new TransferRequest();
                request.setFromAccountNo("A002");  // 注意：这是反向转账
                request.setToAccountNo("A001");    // B转给A
                request.setAmount(new BigDecimal("50"));

                log.info("线程2开始：A002 -> A001 转50元");
                transferService.transfer(request);
                log.info("线程2转账成功");
            } catch (Exception e) {
                log.error("线程2转账失败: {}", e.getMessage());
                checkDeadlock(e, "线程2", deadlockCount);
            } finally {
                latch.countDown();
            }
        });

        log.info("开始并发转账测试...");
        thread1.start();
        thread2.start();

        // 等待两个线程执行完成
        latch.await();

        log.info("测试完成，死锁发生次数: {}", deadlockCount.get());
        log.info("最终账户余额:");
        logAccountBalance();

        thread2.join();
        thread1.join();
    }

    /**
     * 初始化测试数据
     */
    private void initTestData() {
        // 清空表
        accountMapper.deleteByExample(null);

        // 插入测试账户
        Account accountA = new Account();
        accountA.setAccountNo("A001");
        accountA.setBalance(new BigDecimal("1000"));
        accountA.setVersion(0);
        accountMapper.insert(accountA);

        Account accountB = new Account();
        accountB.setAccountNo("A002");
        accountB.setBalance(new BigDecimal("1000"));
        accountB.setVersion(0);
        accountMapper.insert(accountB);

        log.info("初始化数据完成：");
        log.info("A001余额: 1000元");
        log.info("A002余额: 1000元");
    }

    /**
     * 检查是否为死锁异常
     */
    private void checkDeadlock(Exception e, String threadName, AtomicInteger deadlockCount) {
        String message = e.getMessage();
        if (message != null && (
                message.contains("Deadlock") ||
                        message.contains("lock") ||
                        message.contains("回滚") ||
                        message.contains("try restarting transaction")
        )) {
            log.info("{} 遇到死锁: {}", threadName, message);
            deadlockCount.incrementAndGet();
        }
    }

    /**
     * 查询并打印账户余额
     */
    private void logAccountBalance() {
        Account accountA = accountMapper.selectByAccountNo("A001");
        Account accountB = accountMapper.selectByAccountNo("A002");

        if (accountA != null && accountB != null) {
            log.info("A001余额: {} 元", accountA.getBalance());
            log.info("A002余额: {} 元", accountB.getBalance());

            BigDecimal total = accountA.getBalance().add(accountB.getBalance());
            log.info("总余额: {} 元 (应等于2000元)", total);
        }
    }
}

