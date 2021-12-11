package com.aaa.mybatisplus.web.base;

import lombok.Data;

/**
 * 通用的返回值
 *
 * @author liuzhen.tian
 * @version 1.0 CommonResult.java  2021/12/11 20:57
 */
@Data
public class CommonResult<R> {
    private String desc;
    private String code;
    private R data;

    public CommonResult(R data) {
        this.data = data;
        this.code = "200";
        this.desc = "success";
    }

    public CommonResult() {
    }


    public static <R extends CommonResult, T> R success(T result) {
        return (R) new CommonResult<T>(result);
    }
}
