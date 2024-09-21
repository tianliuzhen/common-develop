package com.aaa.commondevelop.web;

import com.aaa.commondevelop.service.AopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author liuzhen.tian
 * @version 1.0 AopController.java  2023/12/30 12:50
 */

@RestController
public class AopController {

    @Autowired
    private AopService aopService;

    @GetMapping("/testAopOrder")
    public void testAopOrder() throws InterruptedException {
        aopService.testAop();
        TimeUnit.MILLISECONDS.sleep(200);
    }

    @GetMapping(value = "/returnStr",produces = {"application/json;charset=utf-8"})
    public String returnStr() {
        return "returnStr";
    }
    @GetMapping("/returnInt")
    public int returnInt() {
        return 1;
    }
}
