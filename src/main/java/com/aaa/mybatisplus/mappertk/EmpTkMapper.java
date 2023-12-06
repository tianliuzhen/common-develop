package com.aaa.mybatisplus.mappertk;

import com.aaa.mybatisplus.domain.entity.Emp;
import com.aaa.mybatisplus.mappertk.common.BatchMapper;
import tk.mybatis.mapper.common.Mapper;

public interface EmpTkMapper extends Mapper<Emp>, BatchMapper<Emp> {

}

