package com.aaa.mybatisplus.test.zhuru;

import org.springframework.stereotype.Service;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/6/16
 */
@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public void helloWord() {
        System.out.println("HelloServiceImpl");
    }
}
