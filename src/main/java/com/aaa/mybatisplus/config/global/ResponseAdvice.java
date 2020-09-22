
package com.aaa.mybatisplus.config.global;


/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/1/14
 */


import com.aaa.mybatisplus.config.httpResult.HttpResult;
import com.aaa.mybatisplus.config.httpResult.RestfulResponse;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import javax.servlet.http.HttpServletRequest;


/**
 * 统一异常处理及返回对象封装
 * @author liuzhen.tian
 * @version 1.0 MyExceptionHandler.java  2020/9/14 10:36
 */
@Slf4j
@ControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    public static final String HTTP_RESULT = "com.aaa.mybatisplus.config.httpResult.HttpResult";

    @Autowired
    HttpServletRequest httpServletRequest;


    /**
     这个方法表示对于哪些请求要执行beforeBodyWrite，返回true执行，返回false不执行
     过滤是否是json的数据
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> aClass) {
        final String returnName = returnType.getParameterType().getName();

        // returnName 运行中报错返回的是  HTTP_RESULT，此时直接返回false 结束
        return !HTTP_RESULT.equals(returnName)
                && !"org.springframework.http.ResponseEntity".equals(returnName);
    }

    /**
     对于返回的对象如果不是最终对象ResponseResult，则选包装一下
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {


        /**
         * case：1
         * 有些接口可能想要的返回值。有的是  result或者有的是 content 等等
         * 防止重复包裹的问题出现
         * 这里的 Response 指的是 com.aaa.mybatisplus.config.configRespone.Response
         * 是我们手动设置的返回值。
         */
        if (body instanceof RestfulResponse){
            return body;
        }
        /**
         * case：2
         * 基本基本数据类型也可以直接返回，唯独String 类型
         * 如果检测到是字符串直接返回字符串，否则会报错
         * 具体原因是：内部没有做判断，字符串还是走的 对象的转换接口，所以就会异常
         */
        if(body instanceof String) {
            return JSON.toJSONString(HttpResult.ok(body));
        }
        /**
         * case：3
         * body = HttpResult  直接返回，一般是异常
         */
        if (body instanceof  HttpResult){
            return body;
        }else {
        /**
         * case：4
         * 正常返回，返回对象封装
         */
            return HttpResult.ok(body);
        }
    }





}

