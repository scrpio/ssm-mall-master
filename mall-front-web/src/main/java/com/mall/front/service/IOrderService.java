package com.mall.front.service;

import com.baomidou.mybatisplus.service.IService;
import com.mall.manager.model.Order;
import com.mall.manager.model.Vo.OrderInfo;
import com.mall.manager.model.Vo.OrderVo;
import com.mall.manager.model.Vo.PageResult;

public interface IOrderService extends IService<Order> {
    /**
     * 分页获得用户订单
     * @param userId
     * @param page
     * @param size
     * @return
     */
    PageResult getOrderList(Long userId, int page, int size);

    /**
     * 获得单个订单
     * @param orderId
     * @return
     */
    OrderVo getOrder(Long orderId);

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    int cancelOrder(Long orderId);

    /**
     * 创建订单
     * @param orderInfo
     * @return
     */
    Long createOrder(OrderInfo orderInfo);

    /**
     * 删除订单
     * @param orderId
     * @return
     */
    int delOrder(Long orderId);
}
