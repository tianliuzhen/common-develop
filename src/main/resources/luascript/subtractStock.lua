--  redis 在2.6版本后原生支持Lua脚本功能，允许开发者使用Lua语言编写脚本传到Redis中执行。
--  redis 预减库存脚本

-- 商品库存key
local  stock_key   = KEYS[1]
local  goods_count = redis.call('get', stock_key)

if tonumber(goods_count, 10) <= 0 then
    return 0
else
    redis.call('decr', stock_key)
end
return 1
