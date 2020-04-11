package com.aaa.mybatisplus.web;

import com.aaa.mybatisplus.config.configGlobalResponse.Shift;
import com.aaa.mybatisplus.enums.ResultCode;
import com.aaa.mybatisplus.enums.common.StatusCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
