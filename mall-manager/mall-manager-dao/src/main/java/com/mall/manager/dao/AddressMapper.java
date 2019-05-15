package com.mall.manager.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mall.manager.model.Address;

import java.util.List;

public interface AddressMapper extends BaseMapper<Address> {
    List<Address> selectAddressByUserId(Long userId);
}