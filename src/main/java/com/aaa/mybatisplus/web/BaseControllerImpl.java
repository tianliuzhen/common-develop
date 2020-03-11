package com.aaa.mybatisplus.web;

import com.aaa.mybatisplus.config.configRespone.ObjectResultResponse;
import com.aaa.mybatisplus.config.configRespone.RestfulResponse;
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


    @PostMapping ("/testStr")
    public ObjectResultResponse<String> testStr() {
            Map map=new HashMap();
            map.put("key","val");
            return  new ObjectResultResponse<>("字符串");

    }
    //不打印响应日志
//    @LessLog(type = LogType.RESPONSE)
    @PostMapping ("/testInt")
    public int testInt(@Valid @RequestParam("i") int i) {
        Map map=new HashMap();
        map.put("key","val");
        return i;

    }

    @PostMapping ("/testMap")
    public Map testMap() {
        Map map=new HashMap();


        map.put("key","val");
        System.out.println("Shift.fatal(DiyResultCode.INVALID_PARAM);");

        return map;

    }
}
