package com.mjs.delong.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandler {


    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        //先记录错误
        log.error(ex.getMessage());

        //判断是否是重复键的错误
        if (ex.getMessage().contains("Duplicate entry")){
            //获取重复的用户名
            String[] split = ex.getMessage().split(" ");
            return R.error(split[2]+" 已存在,请更换");
        }
        return R.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> exceptionhandler(CustomException ex){
        //先记录错误
        log.error(ex.getMessage());
        return R.error(ex.getMessage());
    }
}
