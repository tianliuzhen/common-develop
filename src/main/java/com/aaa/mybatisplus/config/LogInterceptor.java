package com.aaa.mybatisplus.config;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author liuzhen.tian
 * @version 1.0 LogInterceptor.java  2023/2/18 13:55
 */
//自定义日志拦截器：每一次链路，线程维度，添加最终的链路ID ：Trace_ID
@Component
public class LogInterceptor implements HandlerInterceptor {

    public static final String TRACE_ID = "TRACE_ID";

    /**
     * 1.前置拦截器
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String tid = UUID.randomUUID().toString().replace("-", "");
        //1.这里我们是让客户端传入链路ID，然后进行前置拦截捕获
        if (!StringUtils.isEmpty(request.getHeader(TRACE_ID))) {
            tid = request.getHeader(TRACE_ID);
        }
        //2.利用MDC将请求的上下文信息存储到当前线程的上下文映射中
        MDC.put(TRACE_ID, tid);

        response.addHeader(LogInterceptor.TRACE_ID, MDC.get(LogInterceptor.TRACE_ID));
        return true;
    }


    /**
     * 2.后置处理器
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.remove(TRACE_ID);
    }
}
