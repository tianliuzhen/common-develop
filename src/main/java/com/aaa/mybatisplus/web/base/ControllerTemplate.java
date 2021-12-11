package com.aaa.mybatisplus.web.base;

import com.aaa.mybatisplus.config.httpResult.type.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.Optional;
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
     * @param biz                 业务类，用于记录日志
     * @param request             请求值
     * @param callBack            回调类
     * @param response            返回值
     * @param <Q>                 请求类型
     * @param <T>                 执行结果类型
     * @param <R>                 返回值类型
     * @param transactionTemplate 事务
     * @return R
     */
    public <Q, T, R> R execute(String biz, Q request, AbstractExecuteCallBack<Q, T> callBack, R response,
                               TransactionTemplate transactionTemplate) {
        // 1、参数校验
        callBack.check(request);

        // 2、执行业务
        T result = transactionTemplate != null ? transactionTemplate.execute(action -> {
            try {
                return callBack.execute(request);
            } catch (Exception e) {
                action.setRollbackOnly();
                throw e;
            }
        }) : callBack.execute(request);

        // 3、构建结果
        return (R) CommonResult.success(result);
    }

    public <Q, R> R hasTxTemplate(String biz, Q request, Consumer<Q> doCheck, Function<Q, R> doBiz) {
        return (R) execute(biz, request, new AbstractExecuteCallBack<Q, R>() {
            @Override
            protected void check(Q request) {
                super.check(request);
            }

            @Override
            public R execute(Q request) {
                return doBiz.apply(request);
            }
        }, new CommonResult(), transactionTemplate);
    }

    public <Q, R> R noTxTemplate(String biz, Q request, Consumer<Q> doCheck, Function<Q, R> doBiz) {
        return (R) execute(biz, request, new AbstractExecuteCallBack<Q, R>() {
            @Override
            public void check(Q request) {
                doCheck.accept(request);
            }

            @Override
            public R execute(Q request) {
                return doBiz.apply(request);
            }
        }, new CommonResult(), null);
    }

}
