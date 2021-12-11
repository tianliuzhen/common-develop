package com.aaa.mybatisplus.web.base;

import com.baomidou.mybatisplus.extension.api.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author liuzhen.tian
 * @version 1.0 ControllerTemplate.java  2021/12/11 21:01
 */
@Slf4j
@Component
public class ControllerTemplate {
    @Resource
    private TransactionTemplate transactionTemplate;

    /**
     * @param <Q>                 请求类型
     * @param <T>                 执行结果类型
     * @param <R>                 返回值类型
     * @param biz                 业务类，用于记录日志
     * @param request             请求值
     * @param callBack            回调类
     * @param response            返回值
     * @param transactionTemplate 事务
     * @return R
     */
    public <Q, T, R extends CommonResult> R execute(String biz, Q request, AbstractExecuteCallBack<Q, T> callBack, R response,
                                                    TransactionTemplate transactionTemplate) {
        // 1、参数校验
        callBack.check(request);

        // 2、执行业务
        T result = transactionTemplate != null ? transactionTemplate.execute(action -> {
            try {
                return callBack.execute(request);
            } catch (Exception e) {
                // TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
                action.setRollbackOnly();
                log.error(e.getMessage(), e);
                throw e;
            }
        }) : callBack.execute(request);

        // 3、构建结果
        return CommonResult.success(result);
    }

    /**
     * 带事务执行
     *
     * @param biz     业务类，用于记录日志
     * @param request 请求值
     * @param doCheck 前置校验consumer函数
     * @param doBiz   业务执行function1函数
     * @param <Q>     请求值类型
     * @param <T>     执行结果类型
     * @return
     */
    public <Q, T> CommonResult hasTxTemplate(String biz, Q request, Consumer<Q> doCheck, Function<Q, T> doBiz) {
        return execute(biz, request, new AbstractExecuteCallBack<Q, T>() {
            @Override
            protected void check(Q request) {
                super.check(request);
            }

            @Override
            public T execute(Q request) {
                return doBiz.apply(request);
            }
        }, new CommonResult(), transactionTemplate);
    }

    /**
     * 无事务执行
     *
     * @param biz     业务类，用于记录日志
     * @param request 请求值
     * @param doCheck 前置校验consumer函数
     * @param doBiz   业务执行function1函数
     * @param <Q>     请求值类型
     * @param <T>     执行结果类型
     * @return
     */
    public <Q, T> CommonResult noTxTemplate(String biz, Q request, Consumer<Q> doCheck, Function<Q, T> doBiz) {
        return execute(biz, request, new AbstractExecuteCallBack<Q, T>() {
            @Override
            protected void check(Q request) {
                super.check(request);
            }

            @Override
            public T execute(Q request) {
                return doBiz.apply(request);
            }
        }, new CommonResult(), transactionTemplate);
    }

}
