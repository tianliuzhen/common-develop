package com.aaa.mybatisplus.entity;

import com.aaa.mybatisplus.annotation.Check;
import lombok.Data;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/3/12
 */
@Data
public class UserDto {
    /**
     * 姓名
     * */
    private String name;

    /**
     * 性别 man or women
     * */
    @Check(paramValues = {"man", "woman"})
    private String sex;

    public UserDto(String name, String sex) {
        this.name = name;
        this.sex = sex;
    }
}
