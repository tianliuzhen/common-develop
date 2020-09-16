package com.aaa.mybatisplus.config.redis;

import org.apache.commons.lang3.StringUtils;
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

    @Value("${spring.redis.clusters}")
    private String clusters;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        String redisUrl = String.format("redis://%s:%s", hostName + "", port + "");
        config.useSingleServer().setAddress(redisUrl);
        config.useSingleServer().setDatabase(2);
        return Redisson.create(config);
    }


    // @Bean
    // public RedissonClient redissonClient() {
    //     String[] nodes = clusters.split(",");
    //     //redisson版本是3.5，集群的ip前面要加上“redis://”，不然会报错，3.2版本可不加
    //     for(int i=0;i<nodes.length;i++){
    //         nodes[i] = "redis://"+nodes[i];
    //     }
    //     RedissonClient redisson = null;
    //     Config config = new Config();
    //     config.useClusterServers() //这是用的集群server
    //             .setScanInterval(2000) //设置集群状态扫描时间
    //             .addNodeAddress(nodes)
    //     ;
    //     if (StringUtils.isNotBlank(password)) {
    //         config.useSingleServer().setPassword(password);
    //     }
    //     redisson = Redisson.create(config);
    //     //可通过打印redisson.getConfig().toJSON().toString()来检测是否配置成功
    //     return redisson;
    // }
}
