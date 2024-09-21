package com.aaa.commondevelop.config;

import com.aaa.commondevelop.config.snowflakeId.SnowflakeComponent;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 如果需要使用自定义自增id ，implements IdentifierGenerator  即可
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2019-12-20
 */
@Slf4j
@Component
public class MybatisKeyGenerator implements IdentifierGenerator {

    @Value("${server.worker-id}")
    private long workerId;

    @Value("${server.data-center-id}")
    private long dataCenterId;

    @Autowired
    private SnowflakeComponent snowflakeComponent;

    @Override
    public Long nextId(Object entity) {
        //可以将当前传入的class全类名来作为bizKey,或者提取参数来生成bizKey进行分布式Id调用生成.
        String bizKey = entity.getClass().getName();
        //根据bizKey调用分布式ID生成
        // long id = ....;
        //返回生成的id值即可.
        long uid = snowflakeComponent.getInstance().nextId();
        return uid;
    }

}
