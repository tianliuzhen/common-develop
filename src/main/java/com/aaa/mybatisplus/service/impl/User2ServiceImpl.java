package com.aaa.mybatisplus.service.impl;

import com.aaa.mybatisplus.domain.entity.User;
import com.aaa.mybatisplus.mapper.UserMapper;
import com.aaa.mybatisplus.service.User2Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2019/12/17
 */
@Service
public class User2ServiceImpl extends ServiceImpl<UserMapper, User> implements User2Service {
    @Autowired
    private UserMapper userMapper;

    @Override
    public IPage<User> selectUserPage(Page<User> page, String name) {

        // 不进行 count sql 优化，解决 MP 无法自动优化 SQL 问题，这时候你需要自己查询 count 部分
        // page.setOptimizeCountSql(false);
        // 当 total 为小于 0 或者设置 setSearchCount(false) 分页插件不会进行 count 查询
        // 要点!! 分页返回的对象与传入的对象是同一个
        return userMapper.selectPageVo(page, name);
    }
}
