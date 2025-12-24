package com.aaa.commondevelop.service.impl;

import com.aaa.commondevelop.domain.entity.Account;
import com.aaa.commondevelop.domain.request.TransferRequest;
import com.aaa.commondevelop.mapper.tk.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author liuzhen.tian
 * @version 1.0 TransferService.java  2025/12/7 19:19
 */
@Service
@Slf4j
public class TransferService {

    @Autowired
    private AccountMapper accountMapper;

    /**
     * 转账方法（有死锁问题）
     * 真实场景：用户A和用户B互相转账
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean transfer(TransferRequest request) {
        log.info("转账开始: {} -> {} 金额: {}",
                request.getFromAccountNo(),
                request.getToAccountNo(),
                request.getAmount());

        try {
            // 1. 查询转出账户
            Account fromAccount = accountMapper.selectByAccountNo(request.getFromAccountNo());
            if (fromAccount == null) {
                throw new RuntimeException("转出账户不存在: " + request.getFromAccountNo());
            }

            // 2. 查询转入账户
            Account toAccount = accountMapper.selectByAccountNo(request.getToAccountNo());
            if (toAccount == null) {
                throw new RuntimeException("转入账户不存在: " + request.getToAccountNo());
            }

            // 3. 验证余额是否足够
            if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
                throw new RuntimeException("余额不足");
            }

            // 4. 模拟真实业务处理（增加死锁概率）
            // 比如：记录日志、校验风控等
            Thread.sleep(100);

            // 5. 更新转出账户（扣款）
            BigDecimal newFromBalance = fromAccount.getBalance().subtract(request.getAmount());
            fromAccount.setBalance(newFromBalance);
            int updateFromResult = accountMapper.updateBalance(fromAccount);

            if (updateFromResult <= 0) {
                throw new RuntimeException("更新转出账户失败");
            }

            // 6. 模拟再次业务处理
            // Thread.sleep(2000);

            // 7. 更新转入账户（收款）
            BigDecimal newToBalance = toAccount.getBalance().add(request.getAmount());
            toAccount.setBalance(newToBalance);
            int updateToResult = accountMapper.updateBalance(toAccount);

            if (updateToResult <= 0) {
                throw new RuntimeException("更新转入账户失败");
            }

            // 睡眠5s，延迟提交事务，让线程互相等待，更容易复现死锁
            Thread.sleep(1000 * 5);
            log.info("转账成功");
            return true;

        } catch (Exception e) {
            log.error("转账失败: {}", e.getMessage());
            throw new RuntimeException("转账失败: " + e.getMessage());
        }
    }
}
