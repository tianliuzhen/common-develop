
package com.aaa.commondevelop.domain.enums.common;

import com.google.common.collect.ImmutableMap;

/**
 * @author soul
 */
public enum StatusCode implements RestStatus {

    OK(20000, "请求成功"),

    INVALID_MODEL_FIELDS(40001, "字段校验非法"),

    INVALID_PARAMS_CONVERSION(40002, "参数类型非法"),

    HTTP_MESSAGE_NOT_READABLE(41001, "HTTP消息不可读"),

    INVALID_PARAMS_VALIDATE(400003, "参数校验失败"),

    INVALID_PARAMS_CHECK_ERROR(400004, "参数检查失败"),

    RESERVATION_EXPIRE(400005, "预留资源过期"),

    RESOURCE_NOT_EXISTS(400006, "资源不存在"),

    REQUEST_METHOD_NOT_SUPPORTED(41002, "不支持的HTTP请求方法"),

    DUPLICATE_KEY(42001, "操作过快, 请稍后再试"),

    SERVER_UNKNOWN_ERROR(50001, "服务端异常, 请稍后再试"),

    TRADE_MODE_EXCEPTION(52600,"MODE参数非法"),

    TRADE_STRATEGY_EXCEPTION(52601,"STRATEGY参数非法"),

    TRADE_ORDERID_EXCEPTION(52602,"ORDERID参数非法"),

	SEQUENCE_MAX_ERROR(43001, "编号超过最大值");

    private final int code;

    private final String message;

    private static final ImmutableMap<Integer, StatusCode> CACHE;

    static {
        final ImmutableMap.Builder<Integer, StatusCode> builder = ImmutableMap.builder();
        for (StatusCode statusCode : values()) {
            builder.put(statusCode.code(), statusCode);
        }
        CACHE = builder.build();
    }

    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static StatusCode valueOfCode(int code) {
        final StatusCode status = CACHE.get(code);
        if (status == null) {
            throw new IllegalArgumentException("No matching constant for [" + code + "]");
        }
        return status;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

}
