--  redis 秒杀预减库存脚本
--  比较于 subtractStock.lua 限制用户买几件

-- 商品库存key
local  stock_key   = KEYS[1]
-- 购买件数
local  num         = KEYS[2]
-- 用户 userId
local  user_id     = KEYS[3]
-- 限制购买件数
local  limit_num   = KEYS[4]

-- 初始化
redis.call('set', user_id, 0)

-- 1、校验用户购买是否已经达到上限
local userNum=redis.call('get', user_id)
if (userNum >= limit_num) then
    return 0
end;

-- 2、校验库存
local  goods_count = redis.call('get', stock_key)
-- result = 当前库存减去购买件数
local  result  = goods_count - num
if (result >= 0) then
    redis.call('decrby', stock_key,num)
    redis.call('set', user_id, (num + userNum))
    return 1
else
   return 0
end;
