package com.aaa.mybatisplus.config.snowflakeId;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 雪花算法组件配置
 * @author liuzhen.tian
 * @version 1.0 SnowflakeComponent.java  2020/9/16 11:54
 */
@Slf4j
@Component
public class SnowflakeComponent {
    @Value("${server.workerId}")
    private long workerId;
    @Value("${server.datacenterId}")
    private long datacenterId;

    private static volatile SnowflakeIdWorker instance;

    public SnowflakeIdWorker getInstance(){
        if (instance==null) {
            synchronized (SnowflakeIdWorker.class){
                if (instance==null) {
                    log.info("初始化雪花ID生成器, workId = {}, datacenterId = {}", workerId, datacenterId);
                    instance = new SnowflakeIdWorker(workerId, datacenterId);
                }
            }
        }
        return instance;
    }

}
