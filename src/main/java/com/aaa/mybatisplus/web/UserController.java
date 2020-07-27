package com.aaa.mybatisplus.web;

import com.aaa.mybatisplus.annotation.ArgsInfo;
import com.aaa.mybatisplus.annotation.ParameterInfo;
import com.aaa.mybatisplus.config.configGlobalResponse.Shift;
import com.aaa.mybatisplus.entity.BaseMain;
import com.aaa.mybatisplus.enums.ResultCode;
import com.aaa.mybatisplus.enums.common.StatusCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by Ares on 2018/7/5.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/findById")
    public int findById(@RequestParam("id") int id ){
        try {
            if (id>10){
                id = id /0;
            }
        } catch (Exception e) {
            throw new RuntimeException("自定义异常");
//            Shift.fatal(ResultCode.SYSTEM_ERROR);
        }
        getAccountByName("accountCache");
        return  id;
    }


    @Test
    public void testObjectMapper() throws JsonProcessingException {

         ObjectMapper objectMapper =new ObjectMapper();

        String json = "[{\n" +
                "  \"current\": 1,\n" +
                "  \"size\": 2\n" +
                "}]";
        System.out.println(objectMapper.readTree(json).get(0).get("current").toString());
    }

    @Cacheable(value = "user",key = "'user_id_'+#id",unless = "#result == null")
    public String getAccountByName(String id) {
        // 方法内部实现不考虑缓存逻辑，直接实现业务
        System.out.println("real query account."+id);
        return "accountCache";
    }

    @GetMapping(value = "/testParameterInfo")
    public String testParameterInfo(@ParameterInfo String id ){
        return  id;
    }

    /**
     * 测试 aop 拦截方法修改其参数值
     * @param id
     * @return
     */
    @GetMapping("/aopAdd10")
    public int aopAdd10(@RequestParam("id") @ArgsInfo int id){
        System.out.println(id);
        return  id;
    }

    /**
     * 测试 aop 拦截方法修改其返回值
     * @param id
     * @return
     */
    @GetMapping("/aopChangeReturn")
    public String aopChangeReturn(@RequestParam("id") @ArgsInfo String id){
        changeReturn(id);
        return  id;
    }

    public String changeReturn(String id) {

        return id;
    }

    /**
     * 测试返回两种时间格式
     * Date
     * DateTime
     * @return
     */
    @GetMapping("/objectResponseDate")
    public Object response(){
        BaseMain baseMain = new BaseMain();
        baseMain.setDate(new Date());
        baseMain.setLocalDateTime(LocalDateTime.now());

        return baseMain;
    }
}
