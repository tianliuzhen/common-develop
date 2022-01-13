package com.aaa.mybatisplus.config;

import com.aaa.mybatisplus.annotation.config.AccessLimitInterceptor;
import com.aaa.mybatisplus.annotation.config.PageVoParameterResolver;
import com.aaa.mybatisplus.annotation.config.ParameterInfo2Interceptor;
import com.aaa.mybatisplus.annotation.config.ParameterInfoInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ServletWebArgumentResolverAdapter;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 参考：https://blog.csdn.net/weixin_41556963/article/details/103089534
 * 注： 这里实现 WebMvcConfigurationSupport 是单纯为了解决  parameterInfoInterceptor 的问题，但是会影响springBoot 默认配置
 *      因为 extends WebMvcConfigurationSupport 导致 WebMvcAutoConfiguration  不生效
 * 缺陷：
 *     重写WebMvcConfigurationSupport后，springboot默认配置的信息会失效
 *     譬如：utf-8问题（controller 返回值 乱码）、静态资源无法问题。
 * 其他
 *     项目中还可以通过配置 aop 的形式去修改参数值
 *
 * @author liuzhen.tian  time: 2020/6/23 20:57
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AccessLimitInterceptor accessLimitInterceptor;

    @Autowired
    private ParameterInfoInterceptor parameterInfoInterceptor;

    @Autowired
    private ParameterInfo2Interceptor parameterInfo2Interceptor;

    @Autowired
    private PageVoParameterResolver pageVoParameterResolver;

    @Autowired
    private StringHttpMessageConverter stringHttpMessageConverter;
    // @Autowired
    // private JacksonHttpMessageConverter jacksonHttpMessageConverter;

    /**
     * 添加自定义的拦截器
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers){
        /**
         * HandlerMethodArgumentResolver （推荐，spring3.1版本上新增 ）
         */
        argumentResolvers.add(parameterInfoInterceptor);
        argumentResolvers.add(pageVoParameterResolver);

        /**
         * WebArgumentResolver （不推荐）
         */
        argumentResolvers.add(new ServletWebArgumentResolverAdapter(parameterInfo2Interceptor));
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
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        for (int i = 0; i < converters.size(); i++) {
            // 继承 WebMvcConfigurationSupport 默认编码是 ISO 这里修改为 UTF_8
            if (converters.get(i) instanceof StringHttpMessageConverter){
                stringHttpMessageConverter.setDefaultCharset(StandardCharsets.UTF_8);
                converters.set(i, stringHttpMessageConverter);
            }
            // 继承 WebMvcConfigurationSupport 序列化字符串问题
            if (converters.get(i) instanceof MappingJackson2HttpMessageConverter) {
                // converters.set(i, jacksonHttpMessageConverter);
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
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

}
