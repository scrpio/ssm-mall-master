package com.mall.front.service.impl;

import com.google.gson.Gson;
import com.mall.common.jedis.JedisClient;
import com.mall.manager.dao.ItemMapper;
import com.mall.manager.model.Item;
import com.mall.manager.model.Vo.CartProduct;
import com.mall.manager.model.factory.ModelFactory;
import com.mall.front.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private ItemMapper itemDao;
    @Autowired
    private JedisClient jedisClient;
    @Value("${CART_PRE}")
    private String CART_PRE;

    @Override
    public int addCart(long userId, long itemId, int num) {
        //hash: "key:用户id" field："商品id" value："商品信息"
        Boolean hexists = jedisClient.hexists(CART_PRE + ":" + userId, itemId + "");
        //如果存在数量相加
        if (hexists) {
            String json = jedisClient.hget(CART_PRE + ":" + userId, itemId + "");
            if (json != null) {
                CartProduct cartProduct = new Gson().fromJson(json, CartProduct.class);
                cartProduct.setProductNum(cartProduct.getProductNum() + num);
                jedisClient.hset(CART_PRE + ":" + userId, itemId + "", new Gson().toJson(cartProduct));
            } else {
                return 0;
            }
            return 1;
        }
        //如果不存在，根据商品id取商品信息
        Item item = itemDao.selectById(itemId);
        if (item == null) {
            return 0;
        }
        CartProduct cartProduct = ModelFactory.ItemToCarproduct(item);
        cartProduct.setProductNum(num);
        cartProduct.setChecked("checked");
        jedisClient.hset(CART_PRE + ":" + userId, itemId + "", new Gson().toJson(cartProduct));
        return 1;
    }

    @Override
    public List<CartProduct> getCartList(long userId) {
        List<String> jsonList = jedisClient.hvals(CART_PRE + ":" + userId);
        List<CartProduct> list = new ArrayList<CartProduct>();
        for (String json : jsonList) {
            CartProduct cartProduct = new Gson().fromJson(json, CartProduct.class);
            list.add(cartProduct);
        }
        return list;
    }

    @Override
    public int updateCartNum(long userId, long itemId, int num, String checked) {
        String json = jedisClient.hget(CART_PRE + ":" + userId, itemId + "");
        if (json == null) {
            return 0;
        }
        CartProduct cartProduct = new Gson().fromJson(json, CartProduct.class);
        cartProduct.setProductNum(num);
        cartProduct.setChecked(checked);
        jedisClient.hset(CART_PRE + ":" + userId, itemId + "", new Gson().toJson(cartProduct));
        return 1;
    }

    @Override
    public int deleteCartItem(long userId, long itemId) {
        jedisClient.hdel(CART_PRE + ":" + userId, itemId + "");
        return 1;
    }

    @Override
    public int checkAll(long userId, String checked) {
        List<String> jsonList = jedisClient.hvals(CART_PRE + ":" + userId);
        for (String json : jsonList) {
            CartProduct cartProduct = new Gson().fromJson(json, CartProduct.class);
            if ("true".equals(checked)) {
                cartProduct.setChecked("checked");
            } else if ("false".equals(checked)) {
                cartProduct.setChecked("");
            } else {
                return 0;
            }
            jedisClient.hset(CART_PRE + ":" + userId, cartProduct.getProductId() + "", new Gson().toJson(cartProduct));
        }
        return 1;
    }

    @Override
    public int delChecked(long userId) {
        List<String> jsonList = jedisClient.hvals(CART_PRE + ":" + userId);
        for (String json : jsonList) {
            CartProduct cartProduct = new Gson().fromJson(json, CartProduct.class);
            if ("checked".equals(cartProduct.getChecked())) {
                jedisClient.hdel(CART_PRE + ":" + userId, cartProduct.getProductId() + "");
            }
        }
        return 1;
    }
}
