package com.aaa.mybatisplus.config.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * description: 这里实现自定义 选择 库
 *              这里配置用一个ip 的不同的  库
 *              实现多 ip 同理
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/3/25
 */
@Configuration
public class RedisDbMany {


    @Value("${spring.redis.database1}")
    private Integer databaseIndex_1;

    @Value("${spring.redis.database}")
    private Integer databaseIndex;

    @Value("${spring.redis.host}")
    private String hostName;

    @Value("${spring.redis.port}")
    private Integer port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.lettuce.pool.max-idle}")
    private Integer maxIdle;

    @Value("${spring.redis.lettuce.pool.min-idle}")
    private Integer minIdle;

    @Value("${spring.redis.lettuce.pool.max-active}")
    private Integer maxActive;

    @Value("${spring.redis.lettuce.pool.max-wait}")
    private Long maxWait;

    @Value("${spring.redis.timeout}")
    private Long timeOut;

    @Value("${spring.redis.lettuce.shutdown-timeout}")
    private Long shutdownTimeOut;


    /**
     * 自定义LettuceConnectionFactory,这一步的作用就是返回根据你传入参数而配置的
     * LettuceConnectionFactory，
     * 也可以说是LettuceConnectionFactory的原理了，
     * 后面我会详细讲解的,各位同学也可先自己看看源码

     这里定义的方法 createLettuceConnectionFactory，方便快速使用
     */
    private LettuceConnectionFactory createLettuceConnectionFactory(
            int dbIndex, String hostName, int port, String password,
            int maxIdle,int minIdle,int maxActive,
            Long maxWait, Long timeOut,Long shutdownTimeOut){

        //redis配置
        RedisConfiguration redisConfiguration = new RedisStandaloneConfiguration(hostName,port);
        ((RedisStandaloneConfiguration) redisConfiguration).setDatabase(dbIndex);
        ((RedisStandaloneConfiguration) redisConfiguration).setPassword(password);

        //连接池配置
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMinIdle(minIdle);
        genericObjectPoolConfig.setMaxTotal(maxActive);
        genericObjectPoolConfig.setMaxWaitMillis(maxWait);

        //redis客户端配置
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder =
                LettucePoolingClientConfiguration.builder().commandTimeout(Duration.ofMillis(timeOut));

        builder.shutdownTimeout(Duration.ofMillis(shutdownTimeOut));
        builder.poolConfig(genericObjectPoolConfig);
        LettuceClientConfiguration lettuceClientConfiguration = builder.build();

        //根据配置和客户端配置创建连接
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfiguration,lettuceClientConfiguration);
        lettuceConnectionFactory .afterPropertiesSet();

        return lettuceConnectionFactory;
    }

    /**
     *  使用数据库 1
     * @return  RedisTemplate<String,Serializable>r
     */
    @Bean(value="redisTemplate")
    public RedisTemplate<String,Object> getSessionRedisTemplate(){
        //创建客户端连接
        return getStringObjectRedisTemplate(databaseIndex);
    }

    /**
     *  使用数据库 1
     * @return  RedisTemplate<String,Serializable>r
     */
    @Bean(value="redisTemplate1")
    public RedisTemplate<String,Object> getSessionRedisTemplate1(){
        //创建客户端连接
        return getStringObjectRedisTemplate(databaseIndex_1);
    }


    public RedisTemplate<String, Object> getStringObjectRedisTemplate(Integer databaseIndex_12) {
        LettuceConnectionFactory lettuceConnectionFactory =
                createLettuceConnectionFactory
                        (databaseIndex_12, hostName, port, password, maxIdle, minIdle, maxActive, maxWait, timeOut, shutdownTimeOut);
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        FastJson2JsonRedisSerializer<Object> serializer = new FastJson2JsonRedisSerializer<Object>(Object.class);
        // value值的序列化采用fastJsonRedisSerializer
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);
        // key的序列化采用StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setConnectionFactory(lettuceConnectionFactory);
        return template;
    }


}
