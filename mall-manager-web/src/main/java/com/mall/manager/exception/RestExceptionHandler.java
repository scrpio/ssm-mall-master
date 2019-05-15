package com.mall.manager.exception;

import com.mall.common.exception.StoreException;
import com.mall.common.result.Result;
import com.mall.common.result.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.BindException;

@ControllerAdvice
public class RestExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(BindException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Result<Object> bindExceptionHandler(BindException e) {
        String errorMsg = "请求数据校验不合法: ";
        if (e != null) {
            errorMsg = e.getMessage();
            log.warn(errorMsg);
        }
        return new ResultUtil<>().setErrorMsg(errorMsg);
    }

    @ExceptionHandler(StoreException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Result<Object> handleMallException(StoreException e) {
        String errorMsg = "Mall exception:";
        if (e != null) {
            errorMsg = e.getMsg();
            log.warn(errorMsg);
        }
        return new ResultUtil<>().setErrorMsg(errorMsg);
    }
}
