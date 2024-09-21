package com.aaa.commondevelop.annotation.aspect;

import com.aaa.commondevelop.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
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
@Order(value = 1)
@Component
public class SysTimeLogAspect {

    @Pointcut("@within(com.aaa.commondevelop.annotation.SysTimeLog) || @annotation(com.aaa.commondevelop.annotation.SysTimeLog)")
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
            throw e;
        }
        String endTime = TimeUtil.getNowDateDetail();
        log.info(TimeUtil.getNowDateDetail() + " ==================》{}方法 执行结束 ......", joinPoint.getSignature().getName());
        // log.info("共计用时："+TimeUtil.getTimeDifference(beginTime,endTime));

        stopWatch.stop();
        log.info("共计用时：{}{}", stopWatch.getTotalTimeSeconds(), "s");
        return proceed;
    }
}
