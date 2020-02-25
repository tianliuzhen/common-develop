package com.aaa.mybatisplus.test;

import com.aaa.mybatisplus.config.configRespone.Constants;
import lombok.Data;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/1/14
 */
@Data
class A{
    private int code = Constants.DEFAULT_OK;
    //test...
    public A() {
        System.out.println("A的无参数构造器");
    }
    public A(int num) {
        System.out.println("A的有参数构造器,参数为:" + num);
    }
}


