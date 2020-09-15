local lockKey = KEYS[1]
local lockTime = KEYS[2]
local lockValue = KEYS[3]

-- setnx info
local result_1 = redis.call('SETNX', lockKey, lockValue)
if result_1 == 1
then
local result_2 = redis.call('SETEX', lockKey,lockTime, lockValue)
local ok = "OK"
return ok == result_2
else
return 1
end
