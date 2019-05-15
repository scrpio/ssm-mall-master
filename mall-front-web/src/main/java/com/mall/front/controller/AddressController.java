package com.mall.front.controller;

import com.mall.common.result.Result;
import com.mall.common.result.ResultUtil;
import com.mall.manager.model.Address;
import com.mall.front.service.IAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "收货地址")
@RestController
public class AddressController {
    @Autowired
    private IAddressService addressService;

    @ApiOperation(value = "获得所有收货地址")
    @RequestMapping(value = "/member/addressList", method = RequestMethod.POST)
    public Result<List<Address>> addressList(@RequestBody Address address) {

        List<Address> list = addressService.getAddressList(address.getUserId());
        return new ResultUtil<List<Address>>().setData(list);
    }

    @ApiOperation(value = "通过id获得收货地址")
    @RequestMapping(value = "/member/address", method = RequestMethod.POST)
    public Result<Address> address(@RequestBody Address tbAddress) {
        Address address = addressService.getAddress(tbAddress.getAddressId());
        return new ResultUtil<Address>().setData(address);
    }

    @ApiOperation(value = "添加收货地址")
    @RequestMapping(value = "/member/addAddress", method = RequestMethod.POST)
    public Result<Object> addAddress(@RequestBody Address address) {
        int result = addressService.addAddress(address);
        return new ResultUtil<Object>().setData(result);
    }

    @ApiOperation(value = "编辑收货地址")
    @RequestMapping(value = "/member/updateAddress", method = RequestMethod.POST)
    public Result<Object> updateAddress(@RequestBody Address address) {
        int result = addressService.updateAddress(address);
        return new ResultUtil<Object>().setData(result);
    }

    @ApiOperation(value = "删除收货地址")
    @RequestMapping(value = "/member/delAddress", method = RequestMethod.POST)
    public Result<Object> delAddress(@RequestBody Address address) {
        int result = addressService.delAddress(address);
        return new ResultUtil<Object>().setData(result);
    }
}
