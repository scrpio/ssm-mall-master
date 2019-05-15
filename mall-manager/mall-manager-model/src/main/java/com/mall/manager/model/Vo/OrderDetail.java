package com.mall.manager.model.Vo;

import com.mall.manager.model.Order;
import com.mall.manager.model.OrderItem;
import com.mall.manager.model.OrderShipping;

import java.io.Serializable;
import java.util.List;

public class OrderDetail implements Serializable {

    private Order order;

    private List<OrderItem> orderItem;

    private OrderShipping orderShipping;

    public List<OrderItem> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(List<OrderItem> orderItem) {
        this.orderItem = orderItem;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderShipping getOrderShipping() {
        return orderShipping;
    }

    public void setOrderShipping(OrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }
}
