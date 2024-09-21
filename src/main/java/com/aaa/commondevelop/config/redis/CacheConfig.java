package com.aaa.commondevelop.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * description: 该文件说明
 *
 * @author 田留振(tianliuzhen @ haoxiaec.com)
 * @version 1.0
 * @date 2019/6/28
 */
@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {

    private Duration timeToLive = Duration.ZERO;
    public void setTimeToLive(Duration timeToLive) {
        this.timeToLive = timeToLive;
    }

    @Autowired
    private RedisDbMany redisDbMany;
    /***
     * 定义缓存数据 key 生成策略的bean
     *包名+类名+方法名+第一个参数
     ***/
    @Bean
    public KeyGenerator firstParamGenerator(){
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append("mybatis-plus:");
                sb.append(target.getClass().getName());
                sb.append(":");
                sb.append(method.getName());
                if(params.length > 0){
                    sb.append(":");
                    sb.append(params[0]);
                }
                return sb.toString();
            }
        };
    }
    /***
     * 定义缓存数据 key 生成策略的bean
     *包名+类名+方法名（前面为一个文件夹）+第一个参数（这个也为一个文件夹）+ 第二个参数
     ***/
    @Bean
    public KeyGenerator userInfoGenerator(){
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append("mybatis-plus:");
                sb.append(target.getClass().getName());
                sb.append(":");
                sb.append(method.getName());
                sb.append(":");
                sb.append(params[0]);
                if(params.length > 1){
                    sb.append(":");
                    sb.append(params[1]);
                }
                return sb.toString();
            }
        };
    }
    /***
     * 定义缓存数据 key 生成策略的bean
     *包名+类名+方法名+所有参数
     ***/
    @Bean
    public KeyGenerator allParamGenerator(){
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append("mybatis-plus:");
                sb.append(target.getClass().getName());
                sb.append(":");
                sb.append(method.getName());
                for (Object param: params){
                    sb.append(param);
                }
                return sb.toString();
            }
        };
    }

/**
 * 将java对象转换为json字符串
 * @param
 * @return
 */
    @Bean
    public CacheManager cacheManager(LettuceConnectionFactory factory) {

        factory=  redisDbMany.getLettuceConnectionFactoryByIndex(7);

        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        // 可以切换 fastjson 进行序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        //解决查询缓存转换异常的问题
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        // 配置序列化（解决乱码的问题）
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                // 设置缓存的默认过期时间，也是使用Duration设置
                .entryTtl(timeToLive)
                //设置 key为string序列化
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                // 设置value为json序列化
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                // 不缓存空值
                .disableCachingNullValues();

        // 设置一个初始化的缓存空间set集合
        // redis缓存空间实际上就是前缀（：）
        Set<String> cacheNames =  new HashSet<>();
        cacheNames.add("my-redis-cache1");
        cacheNames.add("my-redis-cache2");

        // 对每个缓存空间应用不同的配置
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        configMap.put("my-redis-cache1", config);
        //设置失效时间
        configMap.put("my-redis-cache2", config.entryTtl(Duration.ofSeconds(10)));

        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                // 注意这两句的调用顺序，一定要先调用该方法设置初始化的缓存名，再初始化相关的配置
                .initialCacheNames(cacheNames)
                .withInitialCacheConfigurations(configMap)
                .build();
        return cacheManager;
    }

}
