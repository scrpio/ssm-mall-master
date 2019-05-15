package com.mall.manager.controller;

import com.mall.manager.model.Vo.ResuhSet;
import com.mall.manager.service.IContentService;
import com.mall.common.result.Result;
import com.mall.common.result.ResultUtil;
import com.mall.manager.model.PanelContent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(description = "板块内容管理")
public class ContentController {
    @Autowired
    private IContentService contentService;

    @RequestMapping(value = "/content/list/{panelId}", method = RequestMethod.GET)
    @ApiOperation(value = "通过panelId获得板块内容列表")
    public ResuhSet getContentByCid(@PathVariable int panelId, @RequestParam int page, @RequestParam int limit) {
        ResuhSet result = contentService.getPanelContentListByPanelId(panelId, page, limit);
        return result;
    }

    @RequestMapping(value = "/content/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加板块内容")
    public Result<Object> addContent(@RequestBody PanelContent panelContent) {
        contentService.addPanelContent(panelContent);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/content/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑板块内容")
    public Result<Object> updateContent(@RequestBody PanelContent panelContent) {
        contentService.updateContent(panelContent);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/content/del/{ids}", method = RequestMethod.POST)
    @ApiOperation(value = "删除板块内容")
    public Result<Object> addContent(@PathVariable int[] ids) {
        for (int id : ids) {
            contentService.deletePanelContent(id);
        }
        return new ResultUtil<Object>().setData(null);
    }
}
