package com.aaa.mybatisplus.config;

import com.aaa.mybatisplus.config.snowflakeId.SnowflakeIdWorker;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2019-12-20
 */
@Slf4j
@Component
public class MybatisKeyGenerator implements IKeyGenerator {

    @Value("${server.worker-id}")
    private long workerId;

    @Value("${server.data-center-id}")
    private long dataCenterId;

    @Override
    public String executeSql(String incrementerName) {
        log.info("mybatis plus keyGenerator: " + incrementerName + "(" + workerId + "," + dataCenterId + ")");
        long uid = new SnowflakeIdWorker(workerId, dataCenterId).nextId();
        return "select " + uid + " from dual";
    }

}
