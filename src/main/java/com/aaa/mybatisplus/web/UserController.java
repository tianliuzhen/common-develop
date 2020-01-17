package com.aaa.mybatisplus.web;

import com.aaa.mybatisplus.config.configGlobalException.common.StatusCode;
import com.aaa.mybatisplus.config.configGlobalException.exception.Shift;
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
            System.out.println(e.getMessage());
            Shift.fatal(StatusCode.INVALID_MODEL_FIELDS,e.getMessage());
        }

        return  id;
    }
}
