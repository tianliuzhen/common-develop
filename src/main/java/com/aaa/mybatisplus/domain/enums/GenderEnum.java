package com.aaa.mybatisplus.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * description: 声明通用枚举属性
 *  方式一： 使用 @EnumValue 注解枚举属性 完整示例
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2019/12/20
 */
@Getter
public enum  GenderEnum {

    MALE(0, "男"),
    FEMALE(1, "女"),;

    /**
     * 标记响应json值
     */
    @JsonValue
    private final String desc;

    /**
     * 标记数据库存的值是code
     */
    @EnumValue
    private final int code;
    GenderEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }



}
