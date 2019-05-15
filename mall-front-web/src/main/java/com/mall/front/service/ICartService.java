package com.mall.front.service;

import com.mall.manager.model.Vo.CartProduct;

import java.util.List;

public interface ICartService {
    int addCart(long userId, long itemId, int num);

    List<CartProduct> getCartList(long userId);

    int updateCartNum(long userId, long itemId, int num, String checked);

    int deleteCartItem(long userId, long itemId);

    int checkAll(long userId, String checked);

    int delChecked(long userId);
}
