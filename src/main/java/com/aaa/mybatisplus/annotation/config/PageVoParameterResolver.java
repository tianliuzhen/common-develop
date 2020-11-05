package com.aaa.mybatisplus.annotation.config;

import com.aaa.mybatisplus.annotation.PageAoDefault;
import com.aaa.mybatisplus.domain.ao.PageAo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Field;

/**
 * @author liuzhen.tian
 * @version 1.0 PageVoParameterResolver.java  2020/11/5 15:15
 */
@Component
public class PageVoParameterResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //拦截添加注解的方法参数
        return parameter.hasParameterAnnotation(PageAoDefault.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        PageAoDefault pageAoDefault = parameter.getParameterAnnotation(PageAoDefault.class);
        if (pageAoDefault != null) {
            String orderBy = pageAoDefault.orderBy();
            String direction = pageAoDefault.direction();
            int pageIndex = pageAoDefault.pageIndex();
            int pageSize = pageAoDefault.pageSize();
            PageAo pageAo = new PageAo();
            pageAo.setPageIndex(pageIndex);
            pageAo.setPageSize(pageSize);
            if (StringUtils.isNotBlank(orderBy)) {
                pageAo.setOrderBy(orderBy);
            }
            if (StringUtils.isNotBlank(direction)) {
                pageAo.setDirection(direction);
            }
            //反射获取成员变量们 Field[] getDeclaredFields()
            Field[] fields = parameter.getParameterType().getDeclaredFields();
            for (Field field : fields) {
                String value = webRequest.getParameter(field.getName());
                if (StringUtils.isNotBlank(value)) {
                    //暴力反射（忽略访问权限修饰符的安全检查    ）
                    field.setAccessible(true);
                    Class<?> type = field.getType();
                    if (int.class.equals(type)) {
                        field.set(pageAo, Integer.parseInt(value));
                    } else {
                        field.set(pageAo, value);
                    }
                }
            }
            return pageAo;
        }
        return null;
    }
}
