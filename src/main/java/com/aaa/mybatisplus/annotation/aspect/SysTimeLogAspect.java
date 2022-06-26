package com.aaa.mybatisplus.annotation.aspect;

import com.aaa.mybatisplus.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/3/18
 */

@Slf4j
@Aspect
@Component
public class SysTimeLogAspect {

    @Pointcut("@within(com.aaa.mybatisplus.annotation.SysTimeLog) || @annotation(com.aaa.mybatisplus.annotation.SysTimeLog)")
    public void logPointCut() {
    }


    @Around("logPointCut()")
    public Object doBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object proceed = null;
        String beginTime = TimeUtil.getNowDateDetail();
        log.info(beginTime + " =============》方法 {}开始执行 ......", joinPoint.getSignature().getName());
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        String endTime = TimeUtil.getNowDateDetail();
        log.info(TimeUtil.getNowDateDetail() + " ==================》{}方法 执行结束 ......", joinPoint.getSignature().getName());
        // log.info("共计用时："+TimeUtil.getTimeDifference(beginTime,endTime));

        stopWatch.stop();
        log.info("共计用时：{}{}", stopWatch.getTotalTimeSeconds(), "s");
        return proceed;
    }
}
