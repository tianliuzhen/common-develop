package com.aaa.mybatisplus.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DB implements NameEnum {

    MASTER(1,"master"),

    SLAVE(2,"slave"),
    ;

    private int code;
    private String name;
}
