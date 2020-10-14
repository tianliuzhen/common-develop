package com.aaa.mybatisplus.config.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuzhen.tian
 * @version 1.0 SwaggerProperties.java  2020/10/14 16:27
 */
@Data
@Component
@ConfigurationProperties("swagger")
public class SwaggerProperties {

    /**
     * 是否开启swagger
     **/
    private Boolean enabled;

    /**
     * 标题
     **/
    private String title ;
    /**
     * 描述
     **/
    private String description ;
    /**
     * 版本
     **/
    private String version ;
    /**
     * 许可证
     **/
    private String license ;
    /**
     * 许可证URL
     **/
    private String licenseUrl ;
    /**
     * 服务条款URL
     **/
    private String termsOfServiceUrl ;
    /**
     * swagger会解析的包路径
     **/
    private String basePackage ;

    /**
     * swagger会解析的url规则
     **/
    private List<String> basePath = new ArrayList<>();
    /**
     * 在basePath基础上需要排除的url规则
     **/
    private List<String> excludePath = new ArrayList<>();

    private Contact contact;
    private Authorization authorization;


    @Data
    public static class Contact {
        /**
         * 联系人
         **/
        private String name ;
        /**
         * 联系人url
         **/
        private String url ;
        /**
         * 联系人email
         **/
        private String email ;

    }

    /**
     * securitySchemes 支持方式之一 ApiKey
     */
    @Data
    public static class Authorization {

        /**
         * 鉴权策略ID，对应 SecurityReferences ID
         */
        private String name ;

        /**
         * 鉴权传递的Header参数
         */
        private String keyName ;

        /**
         * 需要开启鉴权URL的正则
         */
        private String authRegex ;
    }
}
