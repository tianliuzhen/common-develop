package com.aaa.commondevelop.web;

import com.aaa.commondevelop.domain.annotation.Assignment;
import com.aaa.commondevelop.domain.annotation.SysLog;
import com.aaa.commondevelop.domain.annotation.SysTimeLog;
import com.aaa.commondevelop.config.LogInterceptor;
import com.aaa.commondevelop.config.httpResult.type.ResultResponse;
import com.aaa.commondevelop.domain.dto.PageDto;
import com.aaa.commondevelop.domain.dto.UserDto;
import com.aaa.commondevelop.domain.entity.Emp;
import com.aaa.commondevelop.domain.entity.FactorRelation;
import com.aaa.commondevelop.domain.entity.People;
import com.aaa.commondevelop.util.CommonUtils;
import com.aaa.commondevelop.util.HttpContextUtils;
import com.aaa.commondevelop.util.ThreadPoolUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

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
@Log4j2
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

    @GetMapping("/returnLongStr2")
    public Object returnLongStr2() {
        Emp factorRelation = new Emp();
        factorRelation.setId(1461642036950462475L);
        factorRelation.setUserName("xxx");
        return JSONObject.parse(JSONObject.toJSONString(factorRelation));
    }

    @GetMapping("/returnLongStr3")
    public Object returnLongStr3() {
        Emp factorRelation = new Emp();
        factorRelation.setId(1461642036950462475L);
        factorRelation.setUserName("xxx");
        log.debug(Thread.currentThread().getName() + "-" + MDC.get(LogInterceptor.TRACE_ID));
        return factorRelation;
    }

    /**
     * 测试多线程异步请求，打印
     *
     * @return
     */
    @GetMapping("/testManyAsyncPool")
    public String testManyAsyncPool() {
        for (int i = 0; i < 10; i++) {
            ThreadPoolUtil.common_pool.execute(() -> {
                log.info(Thread.currentThread().getName() + "-" + MDC.get(LogInterceptor.TRACE_ID));
            });
        }

        return "我是";
    }
}
