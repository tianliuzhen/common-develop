package com.aaa.mybatisplus.redis.mq2;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/3/24
 */

import org.springframework.stereotype.Component;

/**
 * MQ消息处理器
 * @author zhh
 */
@Component
public class MessageReceiver {

    /**
     * 接收消息的方法1
     **/
    public void receiveMessage(String message){
        System.out.println("receiveMessage接收到的消息："+message);
    }

    /**
     * 接收消息的方法2
     **/
    public void receiveMessage2(String message){
        System.out.println("receiveMessage2接收到的消息："+message);
    }

}
