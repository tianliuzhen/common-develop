package com.aaa.commondevelop.config.redis.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * 通过 lua脚本减库存，保证原子性
 *
 * @author liuzhen.tian
 * @version 1.0 SubstartStock.java  2021/11/18 22:16
 */
@Component
public class SubtractStockByLuaImpl implements SubtractStockByLua {

    private DefaultRedisScript<Boolean> subtractStock;

    private DefaultRedisScript<Boolean> seckillSubtractStock;

    /**
     * 初始化加载lua脚本
     */
    @PostConstruct
    public void initLUA() {
        subtractStock = new DefaultRedisScript<>();
        subtractStock.setScriptSource(new ResourceScriptSource(new ClassPathResource("luascript/subtractStock.lua")));
        subtractStock.setResultType(Boolean.class);

        seckillSubtractStock = new DefaultRedisScript<>();
        seckillSubtractStock.setScriptSource(new ResourceScriptSource(new ClassPathResource("luascript/seckillSubtractStock.lua")));
        seckillSubtractStock.setResultType(Boolean.class);
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
    @Override
    public boolean subtractStock(String goodsStockKey, Integer num) {
        List<String> keyList = Arrays.asList(goodsStockKey, String.valueOf(num));
        return (Boolean) redisTemplate.execute(subtractStock, keyList);
    }

    /**
     * 执行减库存：用于秒杀
     *
     * @param goodsStockKey 商品库存key
     * @param num           购买件数
     * @param userId        用户id
     * @param limitNum      限制购买件数
     * @return boolean
     */
    @Override
    public boolean subtractStockLimitNum(String goodsStockKey, Integer num, String userId, Integer limitNum) {
        List<String> keyList = Arrays.asList(goodsStockKey, String.valueOf(num), String.valueOf(userId), String.valueOf(limitNum));
        return (Boolean) redisTemplate.execute(seckillSubtractStock, keyList);
    }
}
