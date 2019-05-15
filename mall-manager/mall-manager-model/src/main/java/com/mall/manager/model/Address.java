package com.mall.manager.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

@TableName("tb_address")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "address_id",type = IdType.AUTO)
    private Long addressId;

    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "user_name")
    private String userName;

    private String tel;

    @TableField(value = "street_name")
    private String streetName;

    private Boolean acquiescence;

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName == null ? null : streetName.trim();
    }

    public Boolean getAcquiescence() {
        return acquiescence;
    }

    public void setAcquiescence(Boolean acquiescence) {
        this.acquiescence = acquiescence;
    }
}