package com.aaa.mybatisplus.config.redis;

import com.aaa.mybatisplus.redis.MessageReceiver;
import com.aaa.mybatisplus.redis.mq.RedisMessagePublisher;
import com.aaa.mybatisplus.redis.mq.RedisMessageSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/3/24
 */
@Configuration
public class RedisMQConfig {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 注入消息监听容器
     * @param connectionFactory 连接工厂
     * @param listenerAdapter   监听处理器1
     * @param listenerAdapter   监听处理器2 (参数名称需和监听处理器的方法名称一致，因为@Bean注解默认注入的id就是方法名称)
     * @return
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter,
                                            MessageListenerAdapter listenerAdapter2,
                                            MessageListenerAdapter listenerAdapter3) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //订阅一个叫mq_01 的信道
        container.addMessageListener(listenerAdapter, new PatternTopic("mq_01"));
        //订阅一个叫mq_02 的信道
        container.addMessageListener(listenerAdapter2, new PatternTopic("mq_02"));
        //这个container 可以添加多个 messageListener
        container.addMessageListener(listenerAdapter3, topic());
        return container;
    }

    /**
     * 消息监听处理器1
     * @param receiver 处理器类
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapter(MessageReceiver receiver) {
        //给messageListenerAdapter 传入一个消息接收的处理器，利用反射的方法调用“receiveMessage”
        // receiveMessage：接收消息的方法名称
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    /**
     * 消息监听处理器2
     * @param receiver 处理器类
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapter2(MessageReceiver receiver) {
        //给messageListenerAdapter 传入一个消息接收的处理器，利用反射的方法调用“receiveMessage2”
        //receiveMessage：接收消息的方法名称
        return new MessageListenerAdapter(receiver, "receiveMessage2");
    }

    /**
     * 消息监听处理器3
     *  处理器类, 采用  implements MessageListener   的自带的
     *                 @Override
     *                onMessage  {
     *
     *                }
     * @return
     */

    @Bean
    MessageListenerAdapter listenerAdapter3() {
        return new MessageListenerAdapter(new RedisMessageSubscriber());
    }


    @Bean
    RedisMessagePublisher redisPublisher() {
        return new RedisMessagePublisher(redisTemplate, topic());
    }

    @Bean
    ChannelTopic topic() {
        return new ChannelTopic("messageQueue");
    }

}
