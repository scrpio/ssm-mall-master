package com.mall.common.model;

import java.io.Serializable;

public class UserToken implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户登录名
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;

    public UserToken(String username, String password) {
        this.username = username;
        this.password = password;
    }

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

    @Override
    public String toString() {
        return "UserToken{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
