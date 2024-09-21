package com.aaa.commondevelop.config.aop.interceptor;

import com.aaa.commondevelop.domain.annotation.ParameterInfo2;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * 在Spring 3.1.0中，ArgumentResolver现在已更改为HandlerMethodArgumentResolver-以前是WebArgumentResolver
 *
 * @author liuzhen.tian
 * @version 1.0 ParameterInfo2Interceptor.java  2022/1/13 21:43
 */
@Component
public class ParameterInfo2Interceptor implements WebArgumentResolver {
    @Override
    public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest nativeWebRequest) throws Exception {
        //获取捕获到的注解
        ParameterInfo2 annotation = methodParameter.getParameterAnnotation(ParameterInfo2.class);

        return "ParameterInfo2";
    }
}
