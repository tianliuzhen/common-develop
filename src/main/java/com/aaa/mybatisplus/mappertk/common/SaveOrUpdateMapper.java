package com.aaa.mybatisplus.mappertk.common;

import org.apache.ibatis.annotations.InsertProvider;

/**
 * @author liuzhen.tian
 * @version 1.0 SaveOrUpdateMapper.java  2023/12/6 22:20
 */
@tk.mybatis.mapper.annotation.RegisterMapper
public interface SaveOrUpdateMapper<T> {
    /**
     * 保存一个实体，如果实体的主键不为null则更新记录, 主键不存在或主键为null, 则插入记录
     *
     * @param record 不能为空
     * @return
     */
    @InsertProvider(type = SaveOrUpdateProvider.class, method = "dynamicSQL")
    int saveOrUpdate(T record);
}
