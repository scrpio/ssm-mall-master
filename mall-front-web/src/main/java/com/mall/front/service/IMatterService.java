package com.mall.front.service;

import com.mall.manager.model.Panel;
import com.mall.manager.model.PanelContent;
import com.mall.manager.model.Vo.PageResult;
import com.mall.manager.model.Vo.ProductDet;
import com.mall.manager.model.Vo.ZTreeNode;

import java.util.List;

public interface IMatterService {

    long viewCount();
    /**
     * 获取导航栏
     * @return
     */
    List<PanelContent> getNavList();
    /**
     * 获取首页数据
     * @return
     */
    List<Panel> getHome();

    /**
     * 获得分类树
     * @param parentId
     * @return
     */
    List<ZTreeNode> getItemCatList(int parentId);
    /**
     * 获取商品推荐板块
     * @return
     */
    List<Panel> getRecommendGoods();

    /**
     * 获取商品详情
     * @param id
     * @return
     */
    ProductDet getProductDet(Long id);

    /**
     * 分页多条件获取全部商品
     * @param page
     * @param size
     * @param sort
     * @param priceGt
     * @param priceLte
     * @return
     */
    PageResult getAllProduct(int page, int size, String sort, Long cid, int priceGt, int priceLte);
}
