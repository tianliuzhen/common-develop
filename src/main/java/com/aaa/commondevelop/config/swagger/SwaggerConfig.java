package com.aaa.commondevelop.config.swagger;


import com.aaa.commondevelop.annotation.SwaggerApi1;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.AntPathMatcher;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * description: Swagger配置
 * 原生的ui：http://localhost:8070/swagger-ui.html  （新版  knife4j 已经去除原生ui这个接口）
 * 美化后的ui：http://localhost:8070/doc.html
 * <p>
 * 坑1：
 * 如果出现异常：java.lang.NullPointerException: null at springfox.documentation.swagger2.mappers.RequestParameterMapper.bodyParameter(RequestParameterMapper.java:264)
 * 则检查  @ApiImplicitParam注解name参数与具体入参是否缺失
 *
 * @version 1.0
 * @date 2019-12-20
 */
@Configuration
//开启swagger2
@EnableSwagger2
//开启EnableKnife4j 优化swagger
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
// prod: swagger.enabled=true  线上环境关闭 swagger
@ConditionalOnProperty(name = "swagger.enabled", havingValue = "true")
public class SwaggerConfig {

    @Autowired
    private SwaggerProperties swaggerProperties;

    @Bean
    public Docket createRestApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("全部接口")
                .apiInfo(apiInfo())
                .select()
                /*RequestHandlerSelectors有几种扫描方式：
                   1.basePackage：指定包扫描
                       示例：.apis(RequestHandlerSelectors.basePackage("com.example.swagger.controller"))
                   2.any:扫描该项目的所有请求链接
                       示例：.apis(RequestHandlerSelectors.any())
                   3.none：所有请求链接都不扫描
                       示例：.apis(RequestHandlerSelectors.none())
                   4.withClassAnnotation:通过类的注解扫描,就是类名上面的注释
                     可以是Controller，RestController,RequestMapping,GetMapping ,PostMapping等等
                       示例：.apis(withClassAnnotation.)
                   5.withMethodAnnotation:通过方法的注解扫描，也就是方法上面的注解
                     可以是RequestMapping ,GetMapping ,PostMapping等等
                       示例：.apis(RequestHandlerSelectors.withMethodAnnotation(RequestMapping.class))
                   一般推荐第一种，其他作为了解即可
                */
                //扫描包路径
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                .paths(PathSelectors.any())
                .build()
                //全局权限验证
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
        return docket;
    }

    /**
     * 1. 根据包进行分组  这里的包是：com.aaa.commondevelop.redis
     */
    @Bean
    public Docket createApi1() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("分组接口一: 指定包名分组")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.aaa.commondevelop.redis"))
                .paths(PathSelectors.any())
                .build()
                //全局权限验证
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                ;
    }

    /**
     * 2. 根据注解分组
     */
    @Bean
    public Docket createApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("分组接口二: 自定义注解分组")
                .apiInfo(apiInfo())
                .select()
                // 注意：这里的注解 区分方法注解和类注解
                .apis(RequestHandlerSelectors.withClassAnnotation(SwaggerApi1.class))
                .paths(PathSelectors.any())
                .build()
                //全局权限验证
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                ;
    }

    /**
     * 3. 根据 路径 进行分组
     * 简单拦截配置：   .paths(PathSelectors.ant("/user/**"))
     * 复杂拦截配置如下：
     */
    @Bean
    public Docket createApi3() {
        // 排除接口
        Set<Predicate<String>> excludePath = new HashSet<>();
        excludePath.add(notAnt("/user/findById"));

        // 包含接口
        Set<Predicate<String>> basePath = new HashSet<>();
        basePath.add(PathSelectors.ant("/user/**"));
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("分组接口三：路径拦截分组")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(basePath.iterator().next())
                .paths(excludePath.iterator().next())
                .build()
                //全局权限验证
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                ;
    }

    /**
     * 排除指定路径
     *
     * @param antPattern
     * @return
     */
    public static Predicate<String> notAnt(final String antPattern) {
        return new Predicate<String>() {
            @Override
            public boolean test(String input) {
                AntPathMatcher matcher = new AntPathMatcher();
                return !matcher.match(antPattern, input);
            }
        };
    }

    public static void main(String[] args) {
        // 匹配指定路径
        Predicate<String> ant = PathSelectors.ant("/user/**");
        System.out.println(ant.test("/user/sd/sd/sd"));

        //正则表达式
        String patternStr = "-?[0-9]+(\\.[0-9]+)?";
        //需要匹配的字符串
        String str = "123";
        Predicate<String> regex = PathSelectors.regex(patternStr);
        System.out.println(regex.test(str));

        // 排除指定路径
        Predicate<String> notAnt = notAnt("/user/**");
        System.out.println(notAnt.test("/user/sd/sd/sd"));
    }

    /**
     * 配置项目简单说明
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 页面标题
                .title(swaggerProperties.getTitle())
                // 描述
                .description(swaggerProperties.getDescription())
                .license(swaggerProperties.getLicense())
                .licenseUrl(swaggerProperties.getLicenseUrl())
                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
                // 版本号
                .version(swaggerProperties.getVersion())
                // 创建人
                .contact(new Contact(
                        swaggerProperties.getContact().getName(),
                        swaggerProperties.getContact().getUrl(),
                        swaggerProperties.getContact().getEmail()
                ))
                .build();
    }

    /**
     * 配置基于 ApiKey 的鉴权对象
     */
    private List<SecurityScheme> securitySchemes() {
        List<SecurityScheme> securitySchemes = new ArrayList<>();
        securitySchemes.add(new ApiKey(
                swaggerProperties.getAuthorization().getName(),
                swaggerProperties.getAuthorization().getKeyName(),
                ApiKeyVehicle.HEADER.getValue()));
        return securitySchemes;
    }

    /**
     * 配置默认的全局鉴权策略的开关，以及通过正则表达式进行匹配；默认 ^.*$ 匹配所有URL
     * 其中 securityReferences 为配置启用的鉴权策略
     */
    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(swaggerProperties.getAuthorization().getAuthRegex())).build());
        return securityContexts;
    }

    /**
     * 配置默认的全局鉴权策略；其中返回的 SecurityReference 中，reference 即为ApiKey对象里面的name，保持一致才能开启全局鉴权
     */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
        return securityReferences;
    }


}
