package com.aaa.mybatisplus.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 通过 lua脚本减库存，保证原子性
 *
 * @author liuzhen.tian
 * @version 1.0 SubstartStock.java  2021/11/18 22:16
 */
@Component
public class SubtractStockByLua {

    private DefaultRedisScript<Boolean> tryLockScript;

    @PostConstruct
    public void initLUA() {
        tryLockScript = new DefaultRedisScript<>();
        tryLockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("luascript/subtractStock.lua")));
        tryLockScript.setResultType(Boolean.class);

    }

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 执行减库存
     *
     * @param goodsStockKey 商品库存key
     * @param num           购买件数
     * @return boolean
     */
    public boolean subtractStock(String goodsStockKey, Integer num) {
        List<String> keyList = Arrays.asList(goodsStockKey, String.valueOf(num));
        return (Boolean) redisTemplate.execute(tryLockScript, keyList);
    }

}
