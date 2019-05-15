package com.mall.manager.controller;

import com.mall.manager.model.Vo.ZTreeNode;
import com.mall.manager.service.IPanelService;
import com.mall.common.result.Result;
import com.mall.common.result.ResultUtil;
import com.mall.manager.model.Panel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(description = "板块列表")
public class PanelController {
    @Autowired
    private IPanelService panelService;

    @RequestMapping(value = "/panel/index/list",method = RequestMethod.GET)
    @ApiOperation(value = "获得首页板块列表不含轮播")
    public List<ZTreeNode> getIndexPanel(){
        List<ZTreeNode> list=panelService.getPanelList(0,false);
        return list;
    }

    @RequestMapping(value = "/panel/indexAll/list",method = RequestMethod.GET)
    @ApiOperation(value = "获得首页板块列表含轮播")
    public List<ZTreeNode> getAllIndexPanel(){
        List<ZTreeNode> list=panelService.getPanelList(0,true);
        return list;
    }

    @RequestMapping(value = "/panel/indexBanner/list",method = RequestMethod.GET)
    @ApiOperation(value = "获得首页轮播板块列表")
    public List<ZTreeNode> getIndexBannerPanel(){
        List<ZTreeNode> list=panelService.getPanelList(-1,true);
        return list;
    }

    @RequestMapping(value = "/panel/other/list",method = RequestMethod.GET)
    @ApiOperation(value = "获得其它添加板块")
    public List<ZTreeNode> getRecommendPanel(){
        List<ZTreeNode> list=panelService.getPanelList(1,false);
        list.addAll(panelService.getPanelList(2,false));
        return list;
    }

    @RequestMapping(value = "/panel/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加板块")
    public Result<Object> addContentCategory(@RequestBody Panel panel){
        panelService.addPanel(panel);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/panel/update",method = RequestMethod.POST)
    @ApiOperation(value = "编辑内容分类")
    public Result<Object> updateContentCategory(@RequestBody Panel panel){
        panelService.updatePanel(panel);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/panel/del/{ids}",method = RequestMethod.POST)
    @ApiOperation(value = "删除内容分类")
    public Result<Object> deleteContentCategory(@PathVariable int[] ids){
        for(int id:ids){
            panelService.deletePanel(id);
        }
        return new ResultUtil<Object>().setData(null);
    }
}
