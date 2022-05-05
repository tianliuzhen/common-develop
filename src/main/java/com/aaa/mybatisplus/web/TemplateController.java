package com.aaa.mybatisplus.web;

import com.aaa.mybatisplus.web.base.CommonResult;
import com.aaa.mybatisplus.web.base.ControllerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuzhen.tian
 * @version 1.0 TemplateController.java  2022/5/5 22:27
 */

@RestController
@RequestMapping(value = "/TemplateController")
public class TemplateController {

    @Autowired
    private ControllerTemplate controllerTemplate;

    @GetMapping("/findById")
    public CommonResult<Integer> findById(String id) {
        return controllerTemplate.noTxTemplate("findById", id, req -> {
            Assert.isNull(req, "为空");
        }, req -> CommonResult.success(200));
    }
}
