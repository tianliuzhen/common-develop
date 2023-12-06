package com.aaa.mybatisplus.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

/**
 * @author liuzhen.tian
 * @version 1.0 WxUser.java  2023/12/6 22:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxUser {
    @Id
    private Long userId;


    private String wxName;

    private String appId;

}
