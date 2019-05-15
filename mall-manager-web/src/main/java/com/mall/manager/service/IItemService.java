package com.mall.manager.service;

import com.baomidou.mybatisplus.service.IService;
import com.mall.manager.model.Vo.ItemVo;
import com.mall.manager.model.Vo.ResuhSet;
import com.mall.manager.model.Item;

public interface IItemService extends IService<Item> {

    /**
     * 通过ID获取商品包含详情
     * @param itemId
     * @return
     */
    ItemVo getItemById(Long itemId);

    /**
     * 通过ID获取不包含详情
     * @param id
     * @return
     */
    Item getNormalItemById(Long id);

    ResuhSet getItemAllList(int page, int limit);
    /**
     * 分页搜索排序获取商品列表
     * @param cid
     * @param search
     * @return
     */
    ResuhSet getItemList(int page, int limit, int cid, String search);

    /**
     * 多条件查询获取商品列表
     * @param draw
     * @param start
     * @param length
     * @param cid
     * @param search
     * @param minDate
     * @param maxDate
     * @param orderCol
     * @param orderDir
     * @return
     */
    ResuhSet getItemSearchList(int draw, int start, int length, int cid,
                               String search, String minDate, String maxDate,
                               String orderCol, String orderDir);

    /**
     * 获取商品总数
     * @return
     */
    Integer getAllItemCount();

    /**
     * 修改商品状态
     * @param id
     * @param state
     * @return
     */
    Item alertItemState(Long id, Integer state);

    /**
     * 彻底删除商品
     * @param id
     * @return
     */
    int deleteItem(Long id);

    /**
     * 添加商品
     * @param itemVo
     * @return
     */
    Item addItem(ItemVo itemVo);

    /**
     * 更新商品
     * @param id
     * @param itemVo
     * @return
     */
    Item updateItem(Long id, ItemVo itemVo);
}
