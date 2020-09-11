package com.aaa.mybatisplus.annotation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 重写WebMvcConfigurationSupport后，springboot默认配置的信息会失效
 * 譬如：utf-8问题（controller 返回值 乱码）、静态资源无法问题。
 * 这里配置 extends WebMvcConfigurationSupport 的作用仅是为了实现自定义的拦截器
 * 项目中还可以通过配置 aop 的形式去修改参数值
 *
 * @author liuzhen.tian  time: 2020/6/23 20:57
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Autowired
    private AccessLimitInterceptor accessLimitInterceptor;
    @Autowired
    private ParameterInfoInterceptor parameterInfoInterceptor;

    /**
     * 添加自定义的拦截器
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers){
        argumentResolvers.add(parameterInfoInterceptor);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessLimitInterceptor);
    }


    /**
     * 中文乱码问题
     * @return
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 解决controller返回字符串中文乱码问题
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter)converter).setDefaultCharset(StandardCharsets.UTF_8);
            }
        }
    }

    // ！！！不能用这种方式 设置utf-8，如果用了将不会添加其他的converter
    // @Override
    // public void configureMessageConverters(List<HttpMessageConverter<?>> converters){
    //     converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
    // }

    /**
     *  extends WebMvcConfigurationSupport 造成问题
     *  静态资源无法访问问题 eg:访问 swagger-ui.html 会出现 404
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }
}
