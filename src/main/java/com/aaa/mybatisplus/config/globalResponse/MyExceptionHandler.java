package com.aaa.mybatisplus.config.globalResponse;

import com.aaa.mybatisplus.config.httpResult.HttpResult;
import com.aaa.mybatisplus.enums.ResultCode;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 全局异常拦截
 * @author liuzhen.tian
 * @version 1.0 MyExceptionHandler.java  2020/9/12 23:50
 */
@Slf4j
@Configuration
public class MyExceptionHandler implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object handler, Exception ex) {
        PrintWriter out = getPrintWrite(response);
        if (ex instanceof RuntimeException) {
            out.write(JSON.toJSONString(HttpResult.fail(ResultCode.SYSTEM_ERROR)));
        } else {
            out.write((JSON.toJSONString("服务器异常")));
        }
        if (null != out) {
            out.close();
        }
        return null;
    }

    private PrintWriter getPrintWrite(HttpServletResponse response) {
        PrintWriter out = null;
        try {
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
        } catch (IOException e) {
            log.error("PrintWriter is exception", e);
        }
        return out;
    }
}
