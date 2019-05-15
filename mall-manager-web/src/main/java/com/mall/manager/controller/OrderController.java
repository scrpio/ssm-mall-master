package com.mall.manager.controller;

import com.mall.manager.model.Vo.OrderDetail;
import com.mall.manager.model.Vo.ResuhSet;
import com.mall.manager.service.IOrderService;
import com.mall.common.result.Result;
import com.mall.common.result.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@Api(description = "订单管理")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    @RequestMapping(value = "/order/list", method = RequestMethod.GET)
    @ApiOperation(value = "获取订单列表")
    public ResuhSet getOrderList(@RequestParam int page, @RequestParam int limit) {
        ResuhSet result = orderService.getOrderList(page, limit);
        return result;
    }

    @RequestMapping(value = "/order/count", method = RequestMethod.GET)
    @ApiOperation(value = "获取订单总数")
    public Result<Object> getOrderCount() {
        Integer result = orderService.countOrder();
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/order/detail/{orderId}", method = RequestMethod.GET)
    @ApiOperation(value = "获取订单详情")
    public Result<Object> getOrderDetail(@PathVariable String orderId) {
        OrderDetail orderDetail = orderService.getOrderDetail(orderId);
        return new ResultUtil<Object>().setData(orderDetail);
    }

    @RequestMapping(value = "/order/remark", method = RequestMethod.POST)
    @ApiOperation(value = "订单备注")
    public Result<Object> remark(@RequestParam String orderId,
                                 @RequestParam(required = false) String message) {
        orderService.remark(orderId, message);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/order/deliver", method = RequestMethod.POST)
    @ApiOperation(value = "订单发货")
    public Result<Object> deliver(@RequestParam String orderId,
                                  @RequestParam String shippingName,
                                  @RequestParam String shippingCode,
                                  @RequestParam BigDecimal postFee) {
        orderService.deliver(orderId, shippingName, shippingCode, postFee);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/order/cancel/{orderId}", method = RequestMethod.GET)
    @ApiOperation(value = "订单取消")
    public Result<Object> cancelOrderByAdmin(@PathVariable String orderId) {
        orderService.cancelOrderByAdmin(orderId);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/order/del/{ids}", method = RequestMethod.POST)
    @ApiOperation(value = "删除订单")
    public Result<Object> getUserInfo(@PathVariable String[] ids) {

        for (String id : ids) {
            orderService.deleteOrder(id);
        }
        return new ResultUtil<Object>().setData(null);
    }
}