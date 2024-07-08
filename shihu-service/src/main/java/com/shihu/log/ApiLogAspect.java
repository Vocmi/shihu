package com.shihu.log;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @Author vocmi
 * @Email 2686782542@qq.com
 * @Date 2024-06-08
 */
@Slf4j
@Aspect
@Component
public class ApiLogAspect {
    @Resource
    private HttpServletRequestProvider httpServletRequestProvider;

    @Around("execution(* com.shihu..controller..*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //用于存放token
        HttpServletRequest req = httpServletRequestProvider.get();
        String reqUri = req.getRequestURI();
        String reqMethod = req.getMethod();
        
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        String clazzName = signature.getDeclaringTypeName();
        Object[] args = joinPoint.getArgs();
        String message = ApiLogConstant.DEFAULT_MESSAGE;
        
        ApiLog apiLog = signature.getMethod().getAnnotation(ApiLog.class);
        if (apiLog != null) {
            message = apiLog.message();
        }
        
        if (StrUtil.isNotBlank(message)) {
            log.info("{} {}, {}#{}, {}, {}.", reqMethod, reqUri, clazzName, methodName, args, message);
        } else {
            log.info("{} {}, {}#{}, {}.", reqMethod, reqUri, clazzName, methodName, args);
        }
        
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();
        long costTimeMillis = stopWatch.getLastTaskTimeMillis();
        
        log.info("{} {}, {}#{}, {}, cost {}ms.", reqMethod, reqUri, clazzName, methodName, args, costTimeMillis);
        
        return result;
    }
}
