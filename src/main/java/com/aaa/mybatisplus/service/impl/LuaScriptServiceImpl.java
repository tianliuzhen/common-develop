package com.aaa.mybatisplus.service.impl;

import com.aaa.mybatisplus.service.LuaScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuzhen.tian
 * @version 1.0 LuaScriptServiceImpl.java  2020/9/14 16:15
 */

@Service
public class LuaScriptServiceImpl implements LuaScriptService {
    @Autowired
    private RedisTemplate redisTemplate;


    private DefaultRedisScript<List> getRedisScript;

    private DefaultRedisScript<Boolean> lockScript;
    @PostConstruct
    public void init(){
        getRedisScript = new DefaultRedisScript<List>();
        getRedisScript.setResultType(List.class);
        getRedisScript.setScriptSource(
                new ResourceScriptSource(new ClassPathResource("luascript/LimitLoadTimes.lua")));

        lockScript = new DefaultRedisScript<Boolean>();
        lockScript.setScriptSource(
                new ResourceScriptSource(new ClassPathResource("luascript/add.lua")));
        lockScript.setResultType(Boolean.class);

    }

    @Override
    public void redisAddScriptExec(){
        /**
         * List设置lua的KEYS
         */
        List<String> keyList = new ArrayList();
        keyList.add("count");
        keyList.add("rate.limiting:127.0.0.1");

        /**
         * 用Mpa设置Lua的ARGV[1]
         */
        Map<String,Object> argvMap = new HashMap<String,Object>();
        argvMap.put("expire",10000);
        argvMap.put("times",10);

        /**
         * 调用脚本并执行
         */
        List result = (List) redisTemplate.execute(getRedisScript,keyList, argvMap);
        System.out.println(result);

    }

    @Override
    public Boolean luaExpress(String key, String time, String value) {

        // 封装参数
        List<Object> keyList = new ArrayList<Object>();
        keyList.add(key);
        keyList.add(time);
        keyList.add(value);
        Boolean result= (Boolean)redisTemplate.execute(lockScript, keyList);
        System.out.println(result);
        return true;
    }
}
