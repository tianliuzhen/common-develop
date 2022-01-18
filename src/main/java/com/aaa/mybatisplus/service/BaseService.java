package com.aaa.mybatisplus.service;

import com.aaa.mybatisplus.domain.entity.Entity;

import java.util.List;

/**
 * @author liuzhen.tian
 * @version 1.0 BaseService.java  2022/1/18 21:40
 */
public interface BaseService {
    List<Entity> queryData();
}
