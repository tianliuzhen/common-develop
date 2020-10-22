package com.aaa.mybatisplus.domain.entity;

import lombok.Data;

/**
 * @author liuzhen.tian
 * @version 1.0 Entity.java  2020/10/20 23:26
 */
@Data
public class Entity {
    private String request;
    private String response;

    public Entity(String request) {
        this.request = request;
    }
}
