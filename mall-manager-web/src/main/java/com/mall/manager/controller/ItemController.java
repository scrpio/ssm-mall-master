package com.mall.manager.controller;

import com.mall.manager.model.Vo.EsInfo;
import com.mall.manager.model.Vo.ItemVo;
import com.mall.manager.model.Vo.ResuhSet;
import com.mall.common.result.Result;
import com.mall.common.result.ResultUtil;
import com.mall.manager.model.Item;
import com.mall.manager.service.IItemService;
import com.mall.manager.service.SearchItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(description = "商品列表管理")
public class ItemController {
    @Autowired
    private IItemService itemService;
    @Autowired
    private SearchItemService searchItemService;

    @ApiOperation(value = "通过ID获取商品")
    @RequestMapping(value = "/item/{itemId}", method = RequestMethod.GET)
    public Result<ItemVo> getItemById(@PathVariable Long itemId) {
        ItemVo itemVo = itemService.getItemById(itemId);
        return new ResultUtil<ItemVo>().setData(itemVo);
    }

    @ApiOperation(value = "获取所有商品")
    @RequestMapping(value = "/item/all", method = RequestMethod.GET)
    public ResuhSet getAllItem(@RequestParam int page, @RequestParam int limit) {
        ResuhSet resuhSet = itemService.getItemAllList(page, limit);
        return resuhSet;
    }

    @ApiOperation(value = "分页搜索排序获取商品列表")
    @RequestMapping(value = "item/list", method = RequestMethod.GET)
    public ResuhSet getItemList(@RequestParam int page, @RequestParam int limit, @RequestParam int cid, @RequestParam String search) {
        ResuhSet resuhSet = itemService.getItemList(page, limit, cid, search);
        return resuhSet;
    }

    @RequestMapping(value = "/item/listSearch", method = RequestMethod.GET)
    @ApiOperation(value = "多条件分页搜索排序获取商品列表")
    public ResuhSet getItemSearchList(int draw, int start, int length, int cid, String searchKey, String minDate, String maxDate,
                                      @RequestParam("search[value]") String search, @RequestParam("order[0][column]") int orderCol,
                                      @RequestParam("order[0][dir]") String orderDir) {
        //获取客户端需要排序的列
        String[] cols = {"checkbox", "id", "image", "title", "sell_point", "price", "created", "updated", "status"};
        //默认排序列
        String orderColumn = cols[orderCol];
        if (orderColumn == null) {
            orderColumn = "created";
        }
        //获取排序方式 默认为desc(asc)
        if (orderDir == null) {
            orderDir = "desc";
        }
        if (!search.isEmpty()) {
            searchKey = search;
        }
        ResuhSet result = itemService.getItemSearchList(draw, start, length, cid, searchKey, minDate, maxDate, orderColumn, orderDir);
        return result;
    }

    @RequestMapping(value = "/item/count", method = RequestMethod.GET)
    @ApiOperation(value = "获得商品总数目")
    public Result<Object> getAllItemCount() {
        Integer result = itemService.getAllItemCount();
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/item/stop/{id}", method = RequestMethod.POST)
    @ApiOperation(value = "下架商品")
    public Result<Item> stopItem(@PathVariable Long id) {
        Item item = itemService.alertItemState(id, 0);
        return new ResultUtil<Item>().setData(item);
    }

    @RequestMapping(value = "/item/start/{id}", method = RequestMethod.POST)
    @ApiOperation(value = "发布商品")
    public Result<Item> startItem(@PathVariable Long id) {
        Item item = itemService.alertItemState(id, 1);
        return new ResultUtil<Item>().setData(item);
    }

    @RequestMapping(value = "/item/del/{ids}", method = RequestMethod.POST)
    @ApiOperation(value = "删除商品")
    public Result<Item> deleteItem(@PathVariable Long[] ids) {
        for (Long id : ids) {
            itemService.deleteItem(id);
        }
        return new ResultUtil<Item>().setData(null);
    }

    @RequestMapping(value = "/item/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加商品")
    public Result<Item> addItem(@RequestBody ItemVo itemVo) {
        Item item = itemService.addItem(itemVo);
        return new ResultUtil<Item>().setData(item);
    }

    @RequestMapping(value = "/item/update/{id}", method = RequestMethod.POST)
    @ApiOperation(value = "编辑商品")
    public Result<Item> updateItem(@PathVariable Long id, ItemVo itemVo) {
        Item item = itemService.updateItem(id, itemVo);
        return new ResultUtil<Item>().setData(item);
    }

    @RequestMapping(value = "/item/importIndex", method = RequestMethod.GET)
    @ApiOperation(value = "导入商品索引至ES")
    public Result<Object> importIndex() {
        searchItemService.importAllItems();
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/es/getInfo", method = RequestMethod.GET)
    @ApiOperation(value = "获取ES信息")
    public Result<Object> getESInfo() {
        EsInfo esInfo = searchItemService.getEsInfo();
        return new ResultUtil<Object>().setData(esInfo);
    }
}
