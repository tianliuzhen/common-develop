package com.aaa.mybatisplus.config.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuzhen.tian
 * @version 1.0 RedssonConfig.java  2020/9/16 16:49
 */
@Configuration
public class RedssonConfig {

    @Value("${spring.redis.host}")
    private String hostName;

    @Value("${spring.redis.port}")
    private Integer port;

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        String redisUrl = String.format("redis://%s:%s", hostName + "", port + "");
        config.useSingleServer().setAddress(redisUrl);
        config.useSingleServer().setDatabase(2);
        return Redisson.create(config);


    }
}
