package com.mall.front.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.mall.manager.model.*;
import com.mall.common.exception.StoreException;
import com.mall.common.jedis.JedisClient;
import com.mall.common.result.ResultCode;
import com.mall.common.util.ToolUtil;
import com.mall.manager.dao.MemberMapper;
import com.mall.manager.dao.OrderItemMapper;
import com.mall.manager.dao.OrderMapper;
import com.mall.manager.dao.OrderShippingMapper;
import com.mall.manager.model.Vo.CartProduct;
import com.mall.manager.model.Vo.OrderInfo;
import com.mall.manager.model.Vo.OrderVo;
import com.mall.manager.model.Vo.PageResult;
import com.mall.manager.model.factory.ModelFactory;
import com.mall.front.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper,Order> implements IOrderService {
    @Autowired
    private MemberMapper memberDao;    //用户
    @Autowired
    private OrderMapper orderDao;    //订单
    @Autowired
    private OrderItemMapper orderItemDao;  //订单商品
    @Autowired
    private OrderShippingMapper orderShippingDao;  //订单物流
    @Autowired
    private JedisClient jedisClient;

    @Value("${CART_PRE}")
    private String CART_PRE;
    @Value("${EMAIL_SENDER}")
    private String EMAIL_SENDER;

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
                if (orderDao.updateById(order) != 1) {
                    throw new StoreException(ResultCode.FAIL);
                }
            } else {
                //返回到期时间
                long time = order.getCreateTime().getTime() + 1000 * 60 * 60 * 24;
                result = String.valueOf(time);
            }
        }
        return result;
    }

    public int getMemberOrderCount(Long userId) {
        EntityWrapper<Order> entityWrapper = new EntityWrapper<Order>();
        Wrapper<Order> wrapper = entityWrapper.eq("user_id", userId);
        List<Order> listOrder = orderDao.selectList(wrapper);
        if (listOrder != null) {
            return listOrder.size();
        }
        return 0;
    }

    @Override
    public PageResult getOrderList(Long userId, int page, int size) {
        //分页
        if (page <= 0) {
            page = 1;
        }
        PageHelper.startPage(page, size);
        PageResult pageOrder = new PageResult();
        List<OrderVo> list = new ArrayList<OrderVo>();
        EntityWrapper<Order> entityWrapper = new EntityWrapper<Order>();
        Wrapper<Order> wrapper = entityWrapper.eq("user_id", userId).orderBy("create_time", false);
        List<Order> listOrder = orderDao.selectList(wrapper);
        for (Order order : listOrder) {
            judgeOrder(order);
            OrderVo orderVo = new OrderVo();
            //orderId
            orderVo.setOrderId(Long.valueOf(order.getOrderId()));
            //orderStatus
            orderVo.setOrderStatus(String.valueOf(order.getStatus()));
            //createDate
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String date = formatter.format(order.getCreateTime());
            orderVo.setCreateDate(date);
            //address
            OrderShipping orderShipping = orderShippingDao.selectById(order.getOrderId());
            if (orderShipping != null){
                Address address = new Address();
                address.setUserName(orderShipping.getReceiverName());
                address.setStreetName(orderShipping.getReceiverAddress());
                address.setTel(orderShipping.getReceiverPhone());
                orderVo.setAddressInfo(address);
            }
            //orderTotal
            if (order.getPayment() == null) {
                orderVo.setOrderTotal(new BigDecimal(0));
            } else {
                orderVo.setOrderTotal(order.getPayment());
            }
            //goodsList
            EntityWrapper<OrderItem> itemEntityWrapper = new EntityWrapper<OrderItem>();
            Wrapper<OrderItem> itemWrapper = itemEntityWrapper.eq("order_id", order.getOrderId());
            List<OrderItem> listItem = orderItemDao.selectList(itemWrapper);
            List<CartProduct> listProduct = new ArrayList<CartProduct>();
            for (OrderItem orderItem : listItem) {
                CartProduct cartProduct = ModelFactory.OrderItemToCartProduct(orderItem);
                listProduct.add(cartProduct);
            }
            orderVo.setGoodsList(listProduct);
            orderVo.setPaymentType(order.getPaymentType());
            list.add(orderVo);
        }
        PageInfo<OrderVo> pageInfo = new PageInfo<OrderVo>(list);
        pageOrder.setTotal(getMemberOrderCount(userId));
        pageOrder.setData(list);
        return pageOrder;
    }

    @Override
    public OrderVo getOrder(Long orderId) {
        OrderVo orderVo = new OrderVo();
        Order order = orderDao.selectById(orderId);
        if (order == null) {
            throw new StoreException(ResultCode.FAIL);
        }
        String validTime = judgeOrder(order);
        if (validTime != null) {
            orderVo.setFinishDate(validTime);
        }
        //orderId
        orderVo.setOrderId(Long.valueOf(order.getOrderId()));
        //orderStatus
        orderVo.setOrderStatus(String.valueOf(order.getStatus()));
        //createDate
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String createDate = formatter.format(order.getCreateTime());
        orderVo.setCreateDate(createDate);
        //payDate
        if (order.getPaymentTime() != null) {
            String payDate = formatter.format(order.getPaymentTime());
            orderVo.setPayDate(payDate);
        }
        //closeDate
        if (order.getCloseTime() != null) {
            String closeDate = formatter.format(order.getCloseTime());
            orderVo.setCloseDate(closeDate);
        }
        //finishDate
        if (order.getEndTime() != null && order.getStatus() == 4) {
            String finishDate = formatter.format(order.getEndTime());
            orderVo.setFinishDate(finishDate);
        }
        //address
        OrderShipping orderShipping = orderShippingDao.selectById(order.getOrderId());
        Address address = new Address();
        address.setUserName(orderShipping.getReceiverName());
        address.setStreetName(orderShipping.getReceiverAddress());
        address.setTel(orderShipping.getReceiverPhone());
        orderVo.setAddressInfo(address);
        //orderTotal
        if (order.getPayment() == null) {
            orderVo.setOrderTotal(new BigDecimal(0));
        } else {
            orderVo.setOrderTotal(order.getPayment());
        }
        //goodsList
        EntityWrapper<OrderItem> entityWrapper = new EntityWrapper<OrderItem>();
        Wrapper<OrderItem> wrapper = entityWrapper.eq("order_id", orderId);
        List<OrderItem> listItem = orderItemDao.selectList(wrapper);
        List<CartProduct> listProduct = new ArrayList<CartProduct>();
        for (OrderItem orderItem : listItem) {
            CartProduct cartProduct = ModelFactory.OrderItemToCartProduct(orderItem);
            listProduct.add(cartProduct);
        }
        orderVo.setGoodsList(listProduct);
        orderVo.setPaymentType(order.getPaymentType());
        return orderVo;
    }

    @Override
    public int cancelOrder(Long orderId) {
        Order order = orderDao.selectById(String.valueOf(orderId));
        if (order == null) {
            throw new StoreException(ResultCode.FAIL);
        }
        order.setStatus(5);
        order.setCloseTime(new Date());
        if (orderDao.updateById(order) != 1) {
            throw new StoreException(ResultCode.FAIL);
        }
        return 1;
    }

    @Override
    public Long createOrder(OrderInfo orderInfo) {
        Member member = memberDao.selectById(Long.valueOf(orderInfo.getUserId()));
        if (member == null) {
            throw new StoreException(ResultCode.FAIL);
        }
        Order order = new Order();
        //生成订单ID
        Long orderId = ToolUtil.getRandomId();
        order.setOrderId(String.valueOf(orderId));
        order.setUserId(Long.valueOf(orderInfo.getUserId()));
        order.setBuyerNick(member.getUsername());
        order.setPayment(orderInfo.getOrderTotal());
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        //0、未付款，1、已付款，2、未发货，3、已发货，4、交易成功，5、交易关闭，6、交易失败
        order.setStatus(0);
        if (orderDao.insert(order) != 1) {
            throw new StoreException(ResultCode.FAIL);
        }
        List<CartProduct> list = orderInfo.getGoodsList();
        for (CartProduct cartProduct : list) {
            OrderItem orderItem = new OrderItem();
            //生成订单商品ID
            Long orderItemId = ToolUtil.getRandomId();
            orderItem.setId(String.valueOf(orderItemId));
            orderItem.setItemId(cartProduct.getProductId());
            orderItem.setOrderId(String.valueOf(orderId));
            orderItem.setNum(Math.toIntExact(cartProduct.getProductNum()));
            orderItem.setPrice(cartProduct.getSalePrice());
            orderItem.setTitle(cartProduct.getProductName());
            orderItem.setPicPath(cartProduct.getProductImg());
            orderItem.setTotalFee(cartProduct.getSalePrice().multiply(BigDecimal.valueOf(cartProduct.getProductNum())));
            orderItem.setCreated(new Date());
            if (orderItemDao.insert(orderItem) != 1) {
                throw new StoreException(ResultCode.FAIL);
            }
            //删除购物车中含该订单的商品
            try {
                List<String> jsonList = jedisClient.hvals(CART_PRE + ":" + orderInfo.getUserId());
                for (String json : jsonList) {
                    CartProduct cart = new Gson().fromJson(json, CartProduct.class);
                    if (cart.getProductId().equals(cartProduct.getProductId())) {
                        jedisClient.hdel(CART_PRE + ":" + orderInfo.getUserId(), cart.getProductId() + "");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //物流表
        OrderShipping orderShipping = new OrderShipping();
        orderShipping.setOrderId(String.valueOf(orderId));
        orderShipping.setReceiverName(orderInfo.getUserName());
        orderShipping.setReceiverAddress(orderInfo.getStreetName());
        orderShipping.setReceiverPhone(orderInfo.getTel());
        orderShipping.setCreated(new Date());
        orderShipping.setUpdated(new Date());
        if (orderShippingDao.insert(orderShipping) != 1) {
            throw new StoreException(ResultCode.FAIL);
        }
        return orderId;
    }

    @Override
    public int delOrder(Long orderId) {
        if (orderDao.deleteById(String.valueOf(orderId)) != 1) {
            throw new StoreException(ResultCode.FAIL);
        }
        EntityWrapper<OrderItem> entityWrapper = new EntityWrapper<OrderItem>();
        Wrapper<OrderItem> wrapper = entityWrapper.eq("order_id", orderId);
        List<OrderItem> list = orderItemDao.selectList(wrapper);
        for (OrderItem orderItem : list) {
            if (orderItemDao.deleteById(orderItem.getId()) != 1) {
                throw new StoreException(ResultCode.FAIL);
            }
        }
        if (orderShippingDao.deleteById(String.valueOf(orderId)) != 1) {
            throw new StoreException(ResultCode.FAIL);
        }
        return 1;
    }
}
