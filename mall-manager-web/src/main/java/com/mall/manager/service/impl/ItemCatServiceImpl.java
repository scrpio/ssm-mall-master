package com.mall.manager.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mall.manager.service.IItemCatService;
import com.mall.common.exception.StoreException;
import com.mall.manager.model.Vo.ZTreeNode;
import com.mall.common.result.ResultCode;
import com.mall.manager.dao.ItemCatMapper;
import com.mall.manager.model.ItemCat;
import com.mall.manager.model.factory.ModelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ItemCatServiceImpl extends ServiceImpl<ItemCatMapper,ItemCat> implements IItemCatService {
    @Autowired
    private ItemCatMapper itemCatMapper;

    @Override
    public ItemCat getItemCatById(Long id) {
        ItemCat itemCat = itemCatMapper.selectById(id);
        if (itemCat == null){
            throw new StoreException(ResultCode.FAIL);
        }
        return itemCat;
    }

    @Override
    public List<ZTreeNode> getItemCatList(int parentId) {
        EntityWrapper<ItemCat> entityWrapper = new EntityWrapper<>();
        Wrapper<ItemCat> wrapper = entityWrapper.orderBy("id");//.eq("parent_id",parentId)
        List<ItemCat> list = itemCatMapper.selectList(wrapper);
        List<ZTreeNode> treeNodeList = new ArrayList<>();
        for (ItemCat itemCat:list){
            ZTreeNode node = ModelFactory.ItemCatToZTreeNode(itemCat);
            treeNodeList.add(node);
        }
        return ZTreeNode.buildList(treeNodeList, 0);
//        return treeNodeList;
    }

    @Override
    public int addItemCat(ItemCat itemCat) {
        if (itemCat.getParentId() == 0){
            itemCat.setSortOrder(0);
            itemCat.setStatus(true);
        }else {
            itemCat.setSortOrder(0);
            itemCat.setStatus(true);
            itemCat.setCreated(new Date());
            itemCat.setUpdated(new Date());
        }
        if (itemCatMapper.insert(itemCat) != 1){
            throw new StoreException(ResultCode.FAIL);
        }
        return 1;
    }

    @Override
    public int updateItemCat(ItemCat itemCat) {
        ItemCat oldItemCat = getItemCatById(itemCat.getId());
        itemCat.setCreated(oldItemCat.getCreated());
        itemCat.setUpdated(new Date());
        if (itemCatMapper.updateById(itemCat) != 1){
            throw new StoreException(ResultCode.FAIL);
        }
        return 1;
    }

    @Override
    public void deleteItemCat(Long id) {
        deleteZTree(id);
    }

    @Override
    public void deleteZTree(Long id) {
        List<ZTreeNode> node = getItemCatList(Math.toIntExact(id));
        if (node.size() > 0){
            for (int i = 0; i < node.size(); i++){
                deleteItemCat((long) node.get(i).getId());
            }
        }
        if (itemCatMapper.deleteById(id) != 1){
            throw new StoreException(ResultCode.FAIL);
        }
    }
}
