package com.aaa.commondevelop.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author liuzhen.tian
 * @version 1.0 Account.java  2025/12/6 20:52
 */
@NoArgsConstructor
@Data
public class Account {
    private Integer id;
    private String accountNo;
    private BigDecimal balance;
    private Integer version;

    public Account(String accountNo, BigDecimal balance) {
        this.accountNo = accountNo;
        this.balance = balance;
    }
}
