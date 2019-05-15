package com.mall.manager.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

@TableName("tb_log")
public class SysLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private Integer type;

    private String url;

    @TableField(value = "request_type")
    private String requestType;

    @TableField(value = "request_param")
    private String requestParam;

    private String user;

    private String ip;

    @TableField(value = "ip_info")
    private String ipInfo;

    private Integer time;

    @TableField(value = "create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /**
     * 设置请求参数
     *
     * @param paramMap
     */
    public void setMapToParams(Map<String, String[]> paramMap) {
        if (paramMap == null || paramMap.size() == 0) {
            return;
        }
        Map<String, Object> params = new HashMap<>();
        for (Map.Entry<String, String[]> param : paramMap.entrySet()) {
            String key = param.getKey();
            String paramValue = (param.getValue() != null ? new Gson().toJson(param.getValue()) : "");
            String obj = StringUtils.endsWithIgnoreCase(param.getKey(), "oldPwd") ? "不可见" : StringUtils.endsWithIgnoreCase(param.getKey(), "password") ? "不可见" : paramValue;
            params.put(key, obj);
        }
        String temp = new Gson().toJson(params);
        if (temp.length() > 255) {
            this.requestParam = temp.substring(0, 254);
        } else {
            this.requestParam = temp;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType == null ? null : requestType.trim();
    }

    public String getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam == null ? null : requestParam.trim();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user == null ? null : user.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getIpInfo() {
        return ipInfo;
    }

    public void setIpInfo(String ipInfo) {
        this.ipInfo = ipInfo == null ? null : ipInfo.trim();
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}