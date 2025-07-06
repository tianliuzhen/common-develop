package com.aaa.commondevelop.mappertk;

import com.aaa.commondevelop.domain.entity.Emp;
import com.aaa.commondevelop.mappertk.common.BatchMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import tk.mybatis.mapper.common.Mapper;

@DS("slave")
public interface EmpTkMapper extends Mapper<Emp>, BatchMapper<Emp> {

}

