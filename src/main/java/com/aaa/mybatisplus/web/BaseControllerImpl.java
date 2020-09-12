package com.aaa.mybatisplus.web;

import com.aaa.mybatisplus.annotation.Assignment;
import com.aaa.mybatisplus.annotation.SysLog;
import com.aaa.mybatisplus.annotation.SysTimeLog;
import com.aaa.mybatisplus.config.httpResult.type.ResultResponse;
import com.aaa.mybatisplus.entity.UserDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/1/14
 */
@RestController
public class BaseControllerImpl  {

    private static   String  str = "default";

    public static UserDto userDto;

    @PostMapping ("/testStr")
    public ResultResponse<String> testStr() {
            Map map=new HashMap();
            map.put("key","val");
            return  new ResultResponse<>("字符串");

    }
    //不打印响应日志
//    @LessLog(type = LogType.RESPONSE)
    @PostMapping ("/testInt")
    @SysLog
    @Assignment
    public int testInt(@Valid @RequestParam("i") int i) {
        Map map=new HashMap();
        map.put("key","val");
        System.out.println(str);
        System.out.println(userDto);
        return i;

    }
    @Assignment
    @PostMapping ("/testMap")
    @SysTimeLog
    public Map testMap() {
        Map map=new HashMap();
        map.put("key","val");
        System.out.println(userDto);
        return map;

    }
}
