package com.aaa.mybatisplus.mappertk;

import com.aaa.mybatisplus.domain.entity.WxUser;
import com.aaa.mybatisplus.mappertk.common.SaveOrUpdateMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author liuzhen.tian
 * @version 1.0 WxUserTkMapper.java  2023/12/6 22:17
 */
public interface WxUserTkMapper extends Mapper<WxUser>, SaveOrUpdateMapper<WxUser> {
}
