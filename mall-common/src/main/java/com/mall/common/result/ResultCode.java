package com.mall.common.result;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */
public enum ResultCode {
    SUCCESS(200,"成功"),
    FAIL(400,"失败"),
    MODIFY_FAIL(400,"修改商品状态失败"),
    UNAUTHORIZED(401,"未认证（签名错误）"),
    NOT_FOUND(404,"接口不存在"),
    INTERNAL_SERVER_ERROR(500,"服务器内部错误"),
    WARNING(501,"上传文件过多，请稍后再传"),
    NOT_USER(404,"用户不存在"),
    PASSWORD_ERROR(500,"密码错误"),
    OVERTIME(500,"用户登录已过期"),
    TAKES(500,"该用户名已被注册");

    private Integer code;
    private String msg;

    ResultCode(Integer code,String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return msg;
    }
}
