package com.aaa.mybatisplus.service.impl;

import com.aaa.mybatisplus.annotation.SysLog;
import com.aaa.mybatisplus.service.AopService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liuzhen.tian
 * @version 1.0 AopServiceImpl.java  2023/12/30 18:18
 */
@Service
public class AopServiceImpl implements AopService {

    @SysLog
    // @SysTimeLog
    @Transactional
    public void testAop() {
        int b = 1 / 0;
    }
}
