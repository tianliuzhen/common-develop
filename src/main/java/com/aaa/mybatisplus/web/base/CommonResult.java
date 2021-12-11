package com.aaa.mybatisplus.web.base;

import com.aaa.mybatisplus.config.httpResult.type.ResultResponse;
import lombok.Data;

/**
 * 通用的返回值
 *
 * @author liuzhen.tian
 * @version 1.0 CommonResult.java  2021/12/11 20:57
 */
@Data
public class CommonResult<T> {
    private String desc;
    private String code;
    private T data;

    public CommonResult(T data) {
        this.data = data;
        this.code = "200";
        this.desc = "success";
    }

    public CommonResult() {
    }

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(data);
    }
}
