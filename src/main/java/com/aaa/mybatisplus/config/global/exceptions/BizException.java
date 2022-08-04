package com.aaa.mybatisplus.config.global.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BizException extends RuntimeException {
    private String errorMsg;
    private int errorCode;

    public BizException(String message,  int errorCode) {
        super(message);
        this.errorMsg = message;
        this.errorCode = errorCode;
    }

    public BizException(String message, int errorCode, Throwable cause) {
        super(message, cause);
        this.errorMsg = message;
        this.errorCode = errorCode;
    }
}


