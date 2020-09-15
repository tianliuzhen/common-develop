-- 加锁脚本

local  lockKey   = KEYS[1]
local  lockTime  = KEYS[2]
local  lockValue = KEYS[3]
local result_1 = redis.call('SETNX', lockKey, lockValue)
if result_1 == 1
then
local result_2 = redis.call('expire', lockKey,lockTime)
return result_2
else
return 0
end;
