package com.aaa.mybatisplus.web;

import com.aaa.mybatisplus.annotation.Assignment;
import com.aaa.mybatisplus.annotation.SysLog;
import com.aaa.mybatisplus.annotation.SysTimeLog;
import com.aaa.mybatisplus.config.httpResult.type.ResultResponse;
import com.aaa.mybatisplus.domain.dto.PageDto;
import com.aaa.mybatisplus.domain.dto.UserDto;
import com.aaa.mybatisplus.domain.entity.Emp;
import com.aaa.mybatisplus.domain.entity.FactorRelation;
import com.aaa.mybatisplus.domain.entity.People;
import com.aaa.mybatisplus.util.CommonUtils;
import com.aaa.mybatisplus.util.HttpContextUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
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
public class BaseControllerImpl {

    private static String str = "default";

    public static UserDto userDto;

    @PostMapping("/testStr")
    public ResultResponse<String> testStr(@Valid @RequestBody PageDto pageDto) {
        Map map = new HashMap();
        map.put("key", "val");
        System.out.println(CommonUtils.getIpAddr(HttpContextUtils.getHttpServletRequest()));
        System.out.println(HttpContextUtils.getDomain());
        System.out.println(HttpContextUtils.getOrigin());
        return new ResultResponse<>("字符串");

    }

    //不打印响应日志
//    @LessLog(type = LogType.RESPONSE)
    @PostMapping("/testInt")
    @SysLog
    @Assignment
    public People testInt(@Valid @RequestParam("i") int i) {
        Map map = new HashMap();
        map.put("key", "val");
        System.out.println(str);
        System.out.println(userDto);
        People people = new People();
        people.setAge(11);
        people.setDate(new Date());
        return people;

    }

    @Assignment
    @PostMapping("/testMap")
    @SysTimeLog
    public Map testMap() {
        Map map = new HashMap();
        map.put("key", "val");
        System.out.println(userDto);
        return map;
    }

    @PostMapping("/returnLongStr")
    public Object returnLongStr() {
        FactorRelation factorRelation = new FactorRelation(1L, 2L, "A");
        return factorRelation;
    }

    @PostMapping("/returnLongStr2")
    public Object returnLongStr2() {
        Emp factorRelation = new Emp();
        factorRelation.setId(111L);

        return JSONObject.parse(JSONObject.toJSONString(factorRelation));
    }
}
