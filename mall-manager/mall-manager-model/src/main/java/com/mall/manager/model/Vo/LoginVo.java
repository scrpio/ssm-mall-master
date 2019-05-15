package com.mall.manager.model.Vo;

import java.io.Serializable;

public class LoginVo implements Serializable {

    /**
     * 会员账号名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    private String phone;

    private String captcha;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
