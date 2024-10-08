package com.aaa.commondevelop.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class People {
    /**
     * 姓名
     */
    private String name;
    /**
     * 年龄
     */
    private Integer age;
    /**
     *  城市信息
     */
    private City city;

    private Boolean sss;

    private List list;

    private Date date;

    public People(String name, Integer age, City city, Boolean sss, List list) {
        this.name = name;
        this.age = age;
        this.city = city;
        this.sss = sss;
        this.list = list;
    }
}
