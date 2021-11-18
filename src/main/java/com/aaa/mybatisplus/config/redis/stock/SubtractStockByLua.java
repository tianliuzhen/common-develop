package com.aaa.mybatisplus.config.redis.stock;

/**
 * @author liuzhen.tian
 * @version 1.0 SubtractStockByLua2.java  2021/11/18 23:04
 */
public interface SubtractStockByLua {
    /**
     * 执行减库存
     *
     * @param goodsStockKey 商品库存key
     * @param num           购买件数
     * @return boolean
     */
    boolean subtractStock(String goodsStockKey, Integer num);

    /**
     * 执行减库存：用于秒杀
     *
     * @param goodsStockKey 商品库存key
     * @param num           购买件数
     * @param userId        用户id
     * @param limitNum      限制购买件数
     * @return boolean
     */
    boolean subtractStockLimitNum(String goodsStockKey, Integer num, String userId, Integer limitNum);
}
