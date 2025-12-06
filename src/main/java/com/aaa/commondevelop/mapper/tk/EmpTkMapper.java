package com.aaa.commondevelop.mapper.tk;

import com.aaa.commondevelop.domain.entity.Emp;
import com.aaa.commondevelop.mapper.tk.common.BatchMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import tk.mybatis.mapper.common.Mapper;

@DS("slave")
public interface EmpTkMapper extends Mapper<Emp>, BatchMapper<Emp> {

}

