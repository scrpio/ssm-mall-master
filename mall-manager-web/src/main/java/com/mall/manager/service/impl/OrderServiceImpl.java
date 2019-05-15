package com.mall.manager.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.manager.dao.ItemCatMapper;
import com.mall.manager.dao.OrderItemMapper;
import com.mall.manager.dao.OrderMapper;
import com.mall.manager.dao.OrderShippingMapper;
import com.mall.manager.model.Vo.OrderDetail;
import com.mall.manager.model.Vo.ResuhSet;
import com.mall.manager.service.IOrderService;
import com.mall.common.exception.StoreException;
import com.mall.common.jedis.JedisClient;
import com.mall.common.result.ResultCode;
import com.mall.manager.model.Order;
import com.mall.manager.model.OrderItem;
import com.mall.manager.model.OrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderShippingMapper orderShippingMapper;
    @Autowired
    private ItemCatMapper itemCatMapper;
    @Autowired
    private JedisClient jedisClient;

    @Override
    public ResuhSet getOrderList(int page, int limit) {
        ResuhSet result = new ResuhSet();
        //分页
        PageHelper.startPage(page, limit);
        EntityWrapper<Order> entityWrapper = new EntityWrapper<>();
        List<Order> list = orderMapper.selectList(entityWrapper);
        PageInfo<Order> pageInfo = new PageInfo<>(list);
        result.setRecordsTotal((int) pageInfo.getTotal());
        result.setSuccess(true);
        result.setData(list);
        return result;
    }

    @Override
    public Integer countOrder() {
        EntityWrapper<Order> entityWrapper = new EntityWrapper<>();
        Integer result = orderMapper.selectCount(entityWrapper);
        if (result == null) {
            throw new StoreException(ResultCode.FAIL);
        }
        return result;
    }

    @Override
    public OrderDetail getOrderDetail(String orderId) {
        OrderDetail orderDetail = new OrderDetail();
        Order order = orderMapper.selectById(orderId);

        EntityWrapper<OrderItem> entityWrapper = new EntityWrapper<>();
        Wrapper<OrderItem> wrapper = entityWrapper.eq("order_id", orderId);
        List<OrderItem> orderItemList = orderItemMapper.selectList(wrapper);

        OrderShipping orderShipping = orderShippingMapper.selectById(orderId);

        orderDetail.setOrder(order);
        orderDetail.setOrderItem(orderItemList);
        orderDetail.setOrderShipping(orderShipping);
        return orderDetail;
    }

    @Override
    public int deliver(String orderId, String shippingName, String shippingCode, BigDecimal postFee) {
        Order order = orderMapper.selectById(orderId);
        order.setShippingName(shippingName);
        order.setShippingCode(shippingCode);
        order.setPostFee(postFee);
        order.setConsignTime(new Date());
        order.setUpdateTime(new Date());
        //之前忘记设置常量了 将就这样看吧 0、未付款，1、已付款，2、未发货，3、已发货，4、交易成功，5、交易关闭
        order.setStatus(3);
        orderMapper.updateById(order);
        return 1;
    }

    @Override
    public int remark(String orderId, String message) {
        Order order = orderMapper.selectById(orderId);
        order.setBuyerMessage(message);
        order.setUpdateTime(new Date());
        orderMapper.updateById(order);
        return 1;
    }

    @Override
    public int cancelOrderByAdmin(String orderId) {
        Order order = orderMapper.selectById(orderId);
        order.setCloseTime(new Date());
        order.setUpdateTime(new Date());
        //之前忘记设置常量了 将就这样看吧 0、未付款，1、已付款，2、未发货，3、已发货，4、交易成功，5、交易关闭
        order.setStatus(5);
        orderMapper.updateById(order);
        return 1;
    }

    @Override
    public int deleteOrder(String id) {
        if (orderMapper.deleteById(id) != 1) {
            throw new StoreException("删除订单数失败");
        }

        EntityWrapper<OrderItem> entityWrapper = new EntityWrapper<>();
        Wrapper<OrderItem> wrapper = entityWrapper.eq("order_id", id);
        List<OrderItem> list = orderItemMapper.selectList(wrapper);
        for (OrderItem orderItem : list) {
            if (orderItemMapper.deleteById(orderItem.getId()) != 1) {
                throw new StoreException("删除订单商品失败");
            }
        }

        if (orderShippingMapper.deleteById(id) != 1) {
            throw new StoreException("删除物流失败");
        }
        return 1;
    }

    @Override
    public int cancelOrder() {
        EntityWrapper<Order> entityWrapper = new EntityWrapper<>();
        List<Order> list = orderMapper.selectList(entityWrapper);
        for (Order order : list) {
            judgeOrder(order);
        }
        return 1;
    }

    /**
     * 判断订单是否超时未支付
     */
    public String judgeOrder(Order order) {
        String result = null;
        if (order.getStatus() == 0) {
            //判断是否已超1天
            long diff = System.currentTimeMillis() - order.getCreateTime().getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            if (days >= 1) {
                //设置失效
                order.setStatus(5);
                order.setCloseTime(new Date());
                if (orderMapper.updateById(order) != 1) {
                    throw new StoreException("设置订单关闭失败");
                }
            } else {
                //返回到期时间
                long time = order.getCreateTime().getTime() + 1000 * 60 * 60 * 24;
                result = String.valueOf(time);
            }
        }
        return result;
    }

}
