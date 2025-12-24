package com.aaa.commondevelop.domain.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liuzhen.tian
 * @version 1.0 TransferRequest.java  2025/12/7 19:20
 */
@Data
public class TransferRequest {
    private String fromAccountNo;
    private String toAccountNo;
    private BigDecimal amount;
}
