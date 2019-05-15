package com.mall.manager.controller;

import com.mall.manager.model.Vo.ZTreeNode;
import com.mall.manager.service.IItemCatService;
import com.mall.common.result.Result;
import com.mall.common.result.ResultUtil;
import com.mall.manager.model.ItemCat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(description = "商品分类管理")
public class ItemCatController {
    @Autowired
    private IItemCatService itemCatService;

    @RequestMapping(value = "/item/cat/list",method = RequestMethod.GET)
    @ApiOperation(value = "通过父ID获取商品分类列表")
    public List<ZTreeNode> getItemCatList(@RequestParam(name="id",defaultValue = "0") int parentId){
        List<ZTreeNode> list=itemCatService.getItemCatList(parentId);
        return list;
    }

    @RequestMapping(value = "/item/cat/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加商品分类")
    public Result<Object> addItemCategory(@RequestBody ItemCat tbItemCat){
        itemCatService.addItemCat(tbItemCat);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/item/cat/update",method = RequestMethod.POST)
    @ApiOperation(value = "编辑商品分类")
    public Result<Object> updateItemCategory(@RequestBody ItemCat tbItemCat){
        itemCatService.updateItemCat(tbItemCat);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/item/cat/del/{id}",method = RequestMethod.POST)
    @ApiOperation(value = "删除商品分类")
    public Result<Object> deleteItemCategory(@PathVariable Long id){
        itemCatService.deleteItemCat(id);
        return new ResultUtil<Object>().setData(null);
    }
}
