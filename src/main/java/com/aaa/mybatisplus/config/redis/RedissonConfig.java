package com.aaa.mybatisplus.config.redis;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author liuzhen.tian
 * @version 1.0 CacheConfiguration.java  2020/9/17 14:57
 */
@Configuration
public class RedissonConfig {

    @Value("${spring.redisson.host}")
    private String hostName;

    @Value("${spring.redisson.port}")
    private Integer port;

    @Value("${spring.redisson.password}")
    private String password;

    @Value("${spring.redisson.clusters}")
    private String clusters;


        /**
         * 单机模式 redisson 客户端
         */
        @Bean(name = "redissonClient")
        @ConditionalOnProperty(name = "spring.redisson.mode", havingValue = "single")
        RedissonClient redissonSingle() {
            Config config = new Config();
            String redisUrl = String.format("redis://%s:%s", hostName + "", port + "");
            SingleServerConfig serverConfig = config.useSingleServer();
            serverConfig.setAddress(redisUrl);
            serverConfig.setDatabase(2);

            if (StringUtils.isNotBlank(password)) {
                serverConfig.setPassword(password);
            }
            return Redisson.create(config);
        }


        /**
         * 集群模式的 redisson 客户端
         *
         * @return
         */
        @Bean(name = "redissonClient")
        @ConditionalOnProperty(name = "spring.redisson.mode", havingValue = "cluster")
        RedissonClient redissonCluster() {
            System.out.println("cluster redisProperties:" + clusters);

            Config config = new Config();
            String[] nodes = clusters.split(",");
            List<String> newNodes = new ArrayList(nodes.length);
            Arrays.stream(nodes).forEach((index) -> newNodes.add(
                    index.startsWith("redis://") ? index : "redis://" + index));

            ClusterServersConfig serverConfig = config.useClusterServers()
                    .addNodeAddress(newNodes.toArray(new String[0]))
                    ;
            if (StringUtils.isNotBlank(password)) {
                serverConfig.setPassword(password);
            }
            return Redisson.create(config);
        }

}

