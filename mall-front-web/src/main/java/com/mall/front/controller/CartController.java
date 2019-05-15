package com.mall.front.controller;

import com.mall.common.result.Result;
import com.mall.common.result.ResultUtil;
import com.mall.manager.model.Vo.Cart;
import com.mall.manager.model.Vo.CartProduct;
import com.mall.front.service.ICartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "购物车")
@RestController
public class CartController {
    @Autowired
    private ICartService cartService;

    @ApiOperation(value = "添加购物车商品")
    @RequestMapping(value = "/member/addCart", method = RequestMethod.POST)
    public Result<Object> addCart(@RequestBody Cart cart) {
        int result = cartService.addCart(cart.getUserId(), cart.getProductId(), cart.getProductNum());
        return new ResultUtil<Object>().setData(result);
    }

    @ApiOperation(value = "获取购物车商品列表")
    @RequestMapping(value = "/member/cartList", method = RequestMethod.POST)
    public Result<List<CartProduct>> getCartList(@RequestBody Cart cart) {
        List<CartProduct> list = cartService.getCartList(cart.getUserId());
        return new ResultUtil<List<CartProduct>>().setData(list);
    }

    @ApiOperation(value = "编辑购物车商品")
    @RequestMapping(value = "/member/cartEdit", method = RequestMethod.POST)
    public Result<Object> updateCartNum(@RequestBody Cart cart) {
        int result = cartService.updateCartNum(cart.getUserId(), cart.getProductId(), cart.getProductNum(), cart.getChecked());
        return new ResultUtil<Object>().setData(result);
    }

    @ApiOperation(value = "是否全选购物车商品")
    @RequestMapping(value = "/member/editCheckAll", method = RequestMethod.POST)
    public Result<Object> editCheckAll(@RequestBody Cart cart) {
        int result = cartService.checkAll(cart.getUserId(), cart.getChecked());
        return new ResultUtil<Object>().setData(result);
    }

    @ApiOperation(value = "删除购物车商品")
    @RequestMapping(value = "/member/cartDel", method = RequestMethod.POST)
    public Result<Object> deleteCartItem(@RequestBody Cart cart) {
        int result = cartService.deleteCartItem(cart.getUserId(), cart.getProductId());
        return new ResultUtil<Object>().setData(result);
    }

    @ApiOperation(value = "删除购物车选中商品")
    @RequestMapping(value = "/member/delCartChecked",method = RequestMethod.POST)
    public Result<Object> delChecked(@RequestBody Cart cart){
        cartService.delChecked(cart.getUserId());
        return new ResultUtil<Object>().setData(null);
    }
}
