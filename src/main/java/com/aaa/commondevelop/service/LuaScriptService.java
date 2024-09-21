package com.aaa.commondevelop.service;

/**
 * @author liuzhen.tian
 * @version 1.0 LuaScriptService.java  2020/9/14 16:15
 */

public interface LuaScriptService {
     void redisAddScriptExec();

     Boolean luaExpress(String key, String time, String value);

}
