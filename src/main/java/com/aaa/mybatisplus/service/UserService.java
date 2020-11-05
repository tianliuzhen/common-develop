package com.aaa.mybatisplus.service;

import com.aaa.mybatisplus.domain.entity.User;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
 * 不加  @DS("master") 默认是 主库
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2019/12/17
 */
@Service
// @DS("master")
public interface UserService  extends IService<User> {
}
