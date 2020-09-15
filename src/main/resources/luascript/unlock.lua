-- 解锁脚本

local lockKey   = KEYS[1]
local lockValue = KEYS[2]
local result_1  = redis.call('get', lockKey)
if result_1 == lockValue
then
local result_2 = redis.call('del', lockKey)
return result_2
else
return false
end;
