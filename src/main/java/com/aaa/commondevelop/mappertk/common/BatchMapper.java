package com.aaa.commondevelop.mappertk.common;


import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.common.special.InsertListMapper;

import java.util.List;

// import tk.mybatis.mapper.additional.insert.InsertListMapper;

/**
 * 注意：批量新增的接口有俩个
 * ------ tk.mybatis.mapper.common.special.InsertListMapper (这个不能指定id，主键必须为递增，回填主键id)
 * ------ tk.mybatis.mapper.additional.insert.InsertListMapper (这个可以指定id，也可以不指定，不回填主键id，一般用这个)
 * 为了满足上面俩个类的条件，重写了方法如下：batchInsert
 *
 * @author liuzhen.tian
 * @version 1.0 BatchMapper.java  2022/11/2 19:45
 */
@tk.mybatis.mapper.annotation.RegisterMapper
public interface BatchMapper<T> extends InsertListMapper<T> {


    /**
     * 根据Example条件批量更新实体`record`包含的不是null的属性值
     *
     * @return
     */
    @UpdateProvider(type = BatchExampleProvider.class, method = "dynamicSQL")
    int batchUpdateByPrimary(List<? extends T> recordList);


    /**
     * 批量插入，支持批量插入的数据库可以使用，例如MySQL,H2等
     * <p>
     * 不支持主键策略，插入前需要设置好主键的值
     * <p>
     * 特别注意：2018-04-22 后，该方法支持 @KeySql 注解的 genId 方式
     *
     * @param recordList
     * @return
     */
    @Options(useGeneratedKeys = true)
    @InsertProvider(type = BatchExampleProvider.class, method = "dynamicSQL")
    int batchInsert(List<? extends T> recordList);
}
