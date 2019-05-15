package com.mall.front.controller;

import com.mall.common.result.Result;
import com.mall.common.result.ResultUtil;
import com.mall.manager.model.Vo.OrderInfo;
import com.mall.manager.model.Vo.OrderVo;
import com.mall.manager.model.Vo.PageResult;
import com.mall.front.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(description = "订单")
@RestController
public class OrderController {
    @Autowired
    private IOrderService orderService;

    @ApiOperation(value = "获得用户所有订单")
    @RequestMapping(value = "/member/orderList", method = RequestMethod.GET)
    public Result<PageResult> getOrderList(String userId,
                                           @RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "5") int size) {
        PageResult pageResult = orderService.getOrderList(Long.valueOf(userId), page, size);
        return new ResultUtil<PageResult>().setData(pageResult);
    }

    @ApiOperation(value = "通过id获取订单")
    @RequestMapping(value = "/member/orderDetail", method = RequestMethod.GET)
    public Result<OrderVo> getOrder(String orderId) {
        OrderVo orderVo = orderService.getOrder(Long.valueOf(orderId));
        return new ResultUtil<OrderVo>().setData(orderVo);
    }

    @ApiOperation(value = "创建订单")
    @RequestMapping(value = "/member/addOrder", method = RequestMethod.POST)
    public Result<Object> addOrder(@RequestBody OrderInfo orderInfo) {
        Long orderId = orderService.createOrder(orderInfo);
        return new ResultUtil<Object>().setData(orderId.toString());
    }

    @ApiOperation(value = "取消订单")
    @RequestMapping(value = "/member/cancelOrder", method = RequestMethod.POST)
    public Result<Object> cancelOrder(@RequestBody OrderVo orderVo) {
        int result = orderService.cancelOrder(orderVo.getOrderId());
        return new ResultUtil<Object>().setData(result);
    }

    @ApiOperation(value = "删除订单")
    @RequestMapping(value = "/member/delOrder", method = RequestMethod.GET)
    public Result<Object> delOrder(String orderId) {
        int result = orderService.delOrder(Long.valueOf(orderId));
        return new ResultUtil<Object>().setData(result);
    }
}
