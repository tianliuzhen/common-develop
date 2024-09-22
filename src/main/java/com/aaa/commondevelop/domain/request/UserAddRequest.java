package com.aaa.commondevelop.domain.request;

import com.aaa.commondevelop.domain.annotation.Check;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

/**
 * @author liuzhen.tian
 * @version 1.0 UserAddRequest.java  2024/9/22 20:43
 */
@Data
public class UserAddRequest {
    /**
     * 自定义注解校验
     */
    @Check(paramValues = {"man", "woman"})
    private String sex;

    @Min(10)
    @Max(100)
    private int age;

    /**
     * @NotNull 指定字段不能为空, 但是可以为空字符串
     * 报错用法："name": null
     * @Size 限制字符串的个数,并且只能作用于字符串
     */
    @NotNull(message = "size not null !")
    @Size(min = 6, max = 12)
    private String nickName;

    @NotEmpty(message = "tag 不能为空")
    private List<String> tagList;

    /**
     * 指定字符串字段不得为空，并且不能为空字符串或者不能为空格
     * 报错用法：
     * "name": " "
     * "name": ""
     * "name": null
     */
    @NotBlank(message = "name not null !")
    private String name;

    /**
     * 指定字符串字段必须是有效的电子邮件地址
     */
    @Email(message = "email format is wrong !")
    private String email;

}
