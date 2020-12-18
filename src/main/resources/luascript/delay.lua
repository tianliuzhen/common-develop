-- 续约脚本

local  lockKey   = KEYS[1]
local  lockTime  = KEYS[2]
local  lockValue = KEYS[3]
local result_1  = redis.call('get', lockKey)
if result_1 == lockValue
then
local result_2 = redis.call('expire', lockKey,lockTime)
return result_2
else
return 0
end;
