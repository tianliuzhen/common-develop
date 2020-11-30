package com.aaa.mybatisplus.web;

import com.aaa.mybatisplus.annotation.SysLog;
import com.aaa.mybatisplus.annotation.SysTimeLog;
import com.aaa.mybatisplus.domain.entity.Entity;
import com.aaa.mybatisplus.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author liuzhen.tian
 * @version 1.0 AsyncController.java  2020/10/20 23:44
 */
@RestController
@SysLog
public class AsyncController {

    @Autowired
    private AsyncService asyncService;

    @GetMapping("/testAsync")
    @SysTimeLog
    public List<Entity> testAsync(){
        return asyncService.queryData();
    }
}
