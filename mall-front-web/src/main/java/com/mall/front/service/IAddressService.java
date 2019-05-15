package com.mall.front.service;

import com.baomidou.mybatisplus.service.IService;
import com.mall.manager.model.Address;

import java.util.List;

public interface IAddressService extends IService<Address> {
    List<Address> getAddressList(Long userId);

    Address getAddress(Long addressId);

    int addAddress(Address address);

    int updateAddress(Address address);

    int delAddress(Address address);
}
