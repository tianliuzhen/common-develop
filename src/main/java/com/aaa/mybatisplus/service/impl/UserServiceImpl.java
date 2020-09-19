package com.aaa.mybatisplus.service.impl;

import com.aaa.mybatisplus.domain.entity.User;
import com.aaa.mybatisplus.mapper.UserMapper;
import com.aaa.mybatisplus.service.UserService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2019/12/17
 */
@Service
@DS("master")
public class UserServiceImpl  extends ServiceImpl<UserMapper, User> implements UserService {

}
