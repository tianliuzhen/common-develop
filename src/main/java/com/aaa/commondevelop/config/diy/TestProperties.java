package com.aaa.commondevelop.config.diy;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.DeprecatedConfigurationProperty;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * description: @PropertySource 中的属性解释
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/1
 *
 * 1.value：指明加载配置文件的路径。
 * 2.ignoreResourceNotFound：指定的配置文件不存在是否报错，默认是false。当设置为 true 时，若该文件不存在，程序不会报错。实际项目开发中，最好设置 ignoreResourceNotFound 为 false。
 * 3.encoding：指定读取属性文件所使用的编码，我们通常使用的是GBK。
 *
 */
@Component
@Data
@PropertySource(value = "classpath:test.properties",ignoreResourceNotFound = false, encoding = "UTF-8", name = "test.properties")
@ConfigurationProperties(prefix = "test")
public class TestProperties {
    String name;
    String oldName;
    String age;

    @Description("该字段已经废弃")
    @DeprecatedConfigurationProperty(reason = "new name",replacement = "替换说明")
    public String getOldName() {
        return oldName;
    }

}
