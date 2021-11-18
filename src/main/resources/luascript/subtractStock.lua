--  redis 预减库存脚本
--  redis 在2.6版本后原生支持Lua脚本功能，允许开发者使用Lua语言编写脚本传到Redis中执行。
--  tonumber(e [, base])：尝试将参数e转换为数字，当不能转换时返回nil，base(2~36)指出参数e当前使用的进制，默认为10进制
--  decrby [key decrement] : 阶段递减

-- 商品库存key
local  stock_key   = KEYS[1]
-- 购买件数
local  num         = KEYS[2]

-- 先查库存
local  goods_count = redis.call('get', stock_key)
-- result = 当前库存减去购买件数
local  result      = tonumber(goods_count, 10) - num

if (result >= 0) then
    redis.call('decrby', stock_key,num)
    return 1
else
   return 0
end;
