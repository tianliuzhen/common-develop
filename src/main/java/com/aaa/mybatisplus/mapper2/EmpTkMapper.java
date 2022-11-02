package com.aaa.mybatisplus.mapper2;

import com.aaa.mybatisplus.domain.entity.Emp;
import com.aaa.mybatisplus.mapper2.common.BatchMapper;
import tk.mybatis.mapper.common.Mapper;

public interface EmpTkMapper extends Mapper<Emp>, BatchMapper<Emp> {

}

