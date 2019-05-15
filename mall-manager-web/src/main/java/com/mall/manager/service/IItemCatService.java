package com.mall.manager.service;

import com.baomidou.mybatisplus.service.IService;
import com.mall.manager.model.Vo.ZTreeNode;
import com.mall.manager.model.ItemCat;

import java.util.List;

public interface IItemCatService extends IService<ItemCat> {

    /**
     * 通过id获取
     * @param id
     * @return
     */
    ItemCat getItemCatById(Long id);

    /**
     * 获得分类树
     * @param parentId
     * @return
     */
    List<ZTreeNode> getItemCatList(int parentId);

    /**
     * 添加分类
     * @param itemCat
     * @return
     */
    int addItemCat(ItemCat itemCat);

    /**
     * 编辑分类
     * @param itemCat
     * @return
     */
    int updateItemCat(ItemCat itemCat);

    /**
     * 删除单个分类
     * @param id
     */
    void deleteItemCat(Long id);

    /**
     * 递归删除
     * @param id
     */
    void deleteZTree(Long id);
}
