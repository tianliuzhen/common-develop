package com.aaa.commondevelop.mappertk;

import com.aaa.commondevelop.domain.entity.Emp;
import com.aaa.commondevelop.mappertk.common.BatchMapper;
import tk.mybatis.mapper.common.Mapper;

public interface EmpTkMapper extends Mapper<Emp>, BatchMapper<Emp> {

}

