package com.aaa.mybatisplus.lua;

import com.aaa.mybatisplus.service.LuaScriptService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author liuzhen.tian
 * @version 1.0 LuaScriptServiceTest.java  2020/9/14 16:21
 */
@SpringBootTest
public class LuaScriptServiceTest {
    @Autowired
    private LuaScriptService luaScriptService;

    @Test
    public void testLuaScriptService(){
        luaScriptService.redisAddScriptExec();
    }
    @Test
    public void testLuaExpress(){
        luaScriptService.luaExpress("ccc","10","123");
    }


}
