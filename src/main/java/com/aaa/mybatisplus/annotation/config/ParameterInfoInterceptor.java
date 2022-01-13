package com.aaa.mybatisplus.annotation.config;

import com.aaa.mybatisplus.annotation.ParameterInfo;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


/**
 * 在Spring 3.1.0中，ArgumentResolver现在已更改为HandlerMethodArgumentResolver-以前是WebArgumentResolver
 *
 * @author liuzhen.tian
 * @version 1.0 2020/6/23 20:49
 */
@Component
public class ParameterInfoInterceptor implements HandlerMethodArgumentResolver {
    //使用自定义的注解
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(ParameterInfo.class);
    }

    //将值注入参数
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        //获取捕获到的注解
        ParameterInfo annotation = methodParameter.getParameterAnnotation(ParameterInfo.class);
        String value = annotation.value();
        //获取需要注入值得逻辑
        //该例子在shiro中获取userId或者用户信息
        if (value == null || "".equalsIgnoreCase(value) || value.equalsIgnoreCase("userId")) {
            return "新的参数";
        } else {
            return value;

        }

    }

}
