package com.aaa.mybatisplus.service;

import com.aaa.mybatisplus.domain.entity.User;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2019/12/17
 */
@Service
@DS("slave")
public interface User2Service extends IService<User> {
    /**
     * 分页测试
     * @param page
     * @param
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.aaa.mybatisplus.domain.entity.User>
     */
     IPage<User> selectUserPage(Page<User> page, String name );
}
