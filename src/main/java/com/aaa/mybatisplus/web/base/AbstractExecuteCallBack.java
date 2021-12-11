package com.aaa.mybatisplus.web.base;

/**
 * @author liuzhen.tian
 * @version 1.0 AbstractExecuteCallBack.java  2021/12/11 21:10
 */
public abstract class AbstractExecuteCallBack<Q, T> {
    /**
     * 执行参数校验，默认不校验
     *
     * @param request 请求值
     */
    protected void check(Q request) {
    }


    /**
     * @param request 请求值
     * @return T 返回值类型
     */
    public abstract T execute(Q request);
}
