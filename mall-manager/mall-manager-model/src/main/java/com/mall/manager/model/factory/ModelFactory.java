package com.mall.manager.model.factory;

import com.mall.manager.model.*;
import com.mall.manager.model.Vo.ItemVo;
import com.mall.manager.model.Vo.ZTreeNode;
import com.mall.manager.model.Vo.CartProduct;
import com.mall.manager.model.Vo.MemberVo;
import com.mall.manager.model.Vo.Product;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;

public class ModelFactory {
    public static Member MemberVoToMember(MemberVo memberVo) {
        if (memberVo == null) {
            return null;
        } else {
            Member member = new Member();
            BeanUtils.copyProperties(memberVo, member);
            return member;
        }
    }

    public static Item ItemVoToItem(ItemVo itemVo) {
        if (itemVo == null) {
            return null;
        } else {
            Item item = new Item();
            BeanUtils.copyProperties(itemVo, item);
            return item;
        }
    }

    public static ItemVo ItemToItemVo(Item item) {
        if (item == null) {
            return null;
        } else {
            ItemVo itemVo = new ItemVo();
            BeanUtils.copyProperties(item, itemVo);
            return itemVo;
        }
    }

    public static Product ItemToProduct(Item item) {
        if (item == null) {
            return null;
        } else {
            Product product = new Product();
            product.setProductId(item.getId());
            product.setProductName(item.getTitle());
            product.setSalePrice(item.getPrice());
            product.setSubTitle(item.getSellPoint());
            product.setLimitNum(item.getLimitNum());
            product.setProductImageBig(item.getImages()[0]);
            return product;
        }
    }

    public static ZTreeNode ItemCatToZTreeNode(ItemCat itemCat) {
        ZTreeNode zTreeNode = new ZTreeNode();
        zTreeNode.setId(Math.toIntExact(itemCat.getId()));
        zTreeNode.setStatus(itemCat.isStatus());
        zTreeNode.setSortOrder(itemCat.getSortOrder());
        zTreeNode.setTitle(itemCat.getName());
        zTreeNode.setpId(Math.toIntExact(itemCat.getParentId()));
        zTreeNode.setIsParent(itemCat.getIsParent());
        zTreeNode.setRemark(itemCat.getRemark());
        zTreeNode.setChildren(new ArrayList<>());
        return zTreeNode;
    }

    public static CartProduct OrderItemToCartProduct(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        } else {
            CartProduct cartProduct = new CartProduct();
//            BeanUtils.copyProperties(orderItem, cartProduct);
            cartProduct.setProductId(orderItem.getItemId());
            cartProduct.setProductName(orderItem.getTitle());
            cartProduct.setSalePrice(orderItem.getTotalFee());
            cartProduct.setProductNum(orderItem.getNum());
            cartProduct.setProductImg(orderItem.getPicPath());
            return cartProduct;
        }
    }

    public static CartProduct ItemToCarproduct(Item item) {
        CartProduct cartProduct = new CartProduct();
        cartProduct.setProductId(item.getId());
        cartProduct.setProductName(item.getTitle());
        cartProduct.setSalePrice(item.getPrice());
        cartProduct.setProductImg(item.getImages()[0]);
        if (item.getLimitNum() == null) {
            cartProduct.setLimitNum(item.getNum());
        } else if (item.getLimitNum() < 0 && item.getNum() < 0) {
            cartProduct.setLimitNum(10);
        } else {
            cartProduct.setLimitNum(item.getLimitNum());
        }
        return cartProduct;
    }

    public static ZTreeNode PanelToZTreeNode(Panel panel) {
        ZTreeNode zTreeNode = new ZTreeNode();
        zTreeNode.setId(panel.getId());
        zTreeNode.setIsParent(false);
        zTreeNode.setpId(0);
        zTreeNode.setTitle(panel.getName());
        zTreeNode.setSortOrder(panel.getSortOrder());
        zTreeNode.setStatus(panel.isStatus());
        zTreeNode.setRemark(panel.getRemark());
        zTreeNode.setLimitNum(panel.getLimitNum());
        zTreeNode.setType(panel.getType());
        zTreeNode.setValue(panel.getType()+"");
        return zTreeNode;
    }

    public static ZTreeNode MenuToZTreeNode(Menu menu) {
        ZTreeNode zTreeNode = new ZTreeNode();
        zTreeNode.setId(menu.getId());
        zTreeNode.setValue(menu.getId()+"");
        zTreeNode.setIsParent(false);
        zTreeNode.setpId(menu.getParentId());
        zTreeNode.setTitle(menu.getName());
        zTreeNode.setChildren(new ArrayList<>());
        return zTreeNode;
    }
}
