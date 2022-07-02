package com.aaa.mybatisplus.mapper;

import com.aaa.mybatisplus.domain.entity.Dept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author liuzhen.tian
 * @version 1.0 DeptMapper.java  2022/7/2 10:09
 */
@Repository
@Mapper
public interface DeptMapper extends BaseMapper<Dept> {
    void insertDept(Dept dept);
}
