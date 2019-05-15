package com.mall.common.exception;

import com.mall.common.result.ResultCode;

public class StoreException extends RuntimeException {
    private String msg;
    private Integer code;
    public StoreException(ResultCode resultCode){
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
    }

    public StoreException(String msg){
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
