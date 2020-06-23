package com.aaa.mybatisplus.annotation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.charset.Charset;
import java.util.List;

/**
 * 重写WebMvcConfigurationSupport后，springboot默认配置的信息会失效
 * 譬如：utf-8问题、静态资源无法问题。
 * @author liuzhen.tian
 * @version 1.0    2020/6/23 20:57
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    //添加自定义的拦截器
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers){
        argumentResolvers.add(new ParameterInfoResolver());
    }


    /**
     * 中文乱码问题
     * @return
     */
    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        //StringHttpMessageConverter是一个请求和响应信息的编码转换器，
        // 通过源码我们发现默认编码ISO-8859-1，不是UTF-8，所以我们只要通过上述配置将请求字符串转为UTF-8 即可
        StringHttpMessageConverter converter = new StringHttpMessageConverter(
                Charset.forName("UTF-8"));
        return converter;
    }
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(responseBodyConverter());
    }
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }



    /**
     * 静态资源无法访问问题 eg:访问 swagger-ui.html 会出现 404
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
        super.addResourceHandlers(registry);
    }

    /**
     * 配置默认servlet处理
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
