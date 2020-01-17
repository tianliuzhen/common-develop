package com.aaa.mybatisplus.config;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2019-12-20
 */
public class test {
    public static void main(String[] args) {

        for (int i = 0; i < 5; i++) {
            long uid = new SnowflakeIdWorker(5, 6).nextId();
            System.out.println(uid);
        }

    }
}
