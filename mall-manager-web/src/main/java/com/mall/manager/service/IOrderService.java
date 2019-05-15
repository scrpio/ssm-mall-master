package com.mall.manager.service;

import com.baomidou.mybatisplus.service.IService;
import com.mall.manager.model.Order;
import com.mall.manager.model.Vo.OrderDetail;
import com.mall.manager.model.Vo.ResuhSet;

import java.math.BigDecimal;

public interface IOrderService extends IService<Order> {
    /**
     * 获得订单列表
     * @param page
     * @param limit
     * @return
     */
    ResuhSet getOrderList(int page, int limit);
    /**
     * 统计订单数
     * @return
     */
    Integer countOrder();

    /**
     * 获取订单详情
     * @param orderId
     * @return
     */
    OrderDetail getOrderDetail(String orderId);

    /**
     * 发货
     * @param orderId
     * @param shippingName
     * @param shippingCode
     * @param postFee
     * @return
     */
    int deliver(String orderId,String shippingName,String shippingCode,BigDecimal postFee);

    /**
     * 备注
     * @param orderId
     * @param message
     * @return
     */
    int remark(String orderId, String message);

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    int cancelOrderByAdmin(String orderId);

    /**
     * 删除订单
     * @param id
     * @return
     */
    int deleteOrder(String id);

    /**
     * 定时取消订单
     */
    int cancelOrder();

}
