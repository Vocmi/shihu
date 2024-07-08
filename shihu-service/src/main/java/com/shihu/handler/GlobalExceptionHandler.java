package com.shihu.handler;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shihu.constant.ErrorConstant;
import shihu.exception.BaseException;
import shihu.result.Result;

/**
 * @Author vocmi
 * @Email 2686782542@qq.com
 * @Date 2024-06-08
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e, HttpServletRequest req) {
        log.error("Catch exception, Request Url: {}, Request Method: {}, Exception Message: {}", req.getRequestURL(), req.getMethod(), e.getMessage());
        return Result.error(ErrorConstant.ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e, HttpServletRequest req) {
        log.error("Catch runtime exception, Request Url: {}, Request Method: {}, Exception Message: {}", req.getRequestURL(), req.getMethod(), e.getMessage());
        return Result.error(ErrorConstant.ERROR);
    }

    @ExceptionHandler(BaseException.class)
    public Result handleBaseException(BaseException e, HttpServletRequest req) {
        log.error("Catch base exception, Request Url: {}, Request Method: {}, Exception Class: {}, Exception Message: {}", req.getRequestURL(), req.getMethod(), e.getClass(), e.getMessage());
        return Result.error(e.getMessage());
    }
}