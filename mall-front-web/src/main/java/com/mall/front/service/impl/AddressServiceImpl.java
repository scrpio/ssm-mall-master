package com.mall.front.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mall.common.exception.StoreException;
import com.mall.common.result.ResultCode;
import com.mall.manager.dao.AddressMapper;
import com.mall.manager.model.Address;
import com.mall.front.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper,Address> implements IAddressService {
    @Autowired
    private AddressMapper addressDao;

    private void setOneDefault(Address address) {
        //设置唯一默认
        if (address.getAcquiescence()) {
            List<Address> list = addressDao.selectAddressByUserId(address.getUserId());
            for (Address tbAddress : list) {
                tbAddress.setAcquiescence(false);
                addressDao.updateAllColumnById(tbAddress);
            }
        }
    }

    @Override
    public List<Address> getAddressList(Long userId) {
        List<Address> list = addressDao.selectAddressByUserId(userId);
        if (list == null) {
            throw new StoreException(ResultCode.FAIL);
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getAcquiescence()) {
                Collections.swap(list, 0, i);
                break;
            }
        }
        return list;
    }

    @Override
    public Address getAddress(Long addressId) {
        Address address = addressDao.selectById(addressId);
        if (address == null) {
            throw new StoreException(ResultCode.FAIL);
        }
        return address;
    }

    @Override
    public int addAddress(Address address) {
        //设置唯一默认
        setOneDefault(address);
        if (addressDao.insert(address) != 1) {
            throw new StoreException(ResultCode.FAIL);
        }
        return 1;
    }

    @Override
    public int updateAddress(Address address) {
        //设置唯一默认
        setOneDefault(address);
        if (addressDao.updateAllColumnById(address) != 1) {
            throw new StoreException(ResultCode.FAIL);
        }
        return 1;
    }

    @Override
    public int delAddress(Address address) {
        if (addressDao.deleteById(address.getAddressId()) != 1) {
            throw new StoreException(ResultCode.FAIL);
        }
        return 1;
    }
}
