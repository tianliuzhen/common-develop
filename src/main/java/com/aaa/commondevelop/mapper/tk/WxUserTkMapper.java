package com.aaa.commondevelop.mapper.tk;

import com.aaa.commondevelop.domain.entity.WxUser;
import com.aaa.commondevelop.mapper.tk.common.SaveOrUpdateMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author liuzhen.tian
 * @version 1.0 WxUserTkMapper.java  2023/12/6 22:17
 */
public interface WxUserTkMapper extends Mapper<WxUser>, SaveOrUpdateMapper<WxUser> {
}
