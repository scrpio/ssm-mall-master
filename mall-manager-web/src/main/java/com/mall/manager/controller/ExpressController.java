package com.mall.manager.controller;

import com.mall.manager.service.IExpressService;
import com.mall.common.result.Result;
import com.mall.common.result.ResultUtil;
import com.mall.manager.model.Express;
import com.mall.manager.model.Vo.ResuhSet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(description = "快递")
public class ExpressController {
    @Autowired
    private IExpressService expressService;

    @RequestMapping(value = "/express/list",method = RequestMethod.GET)
    @ApiOperation(value = "分页获取所有快递")
    public ResuhSet expressList(@RequestParam int page, @RequestParam int limit){
        return expressService.getExpressList(page, limit);
    }

    @RequestMapping(value = "/express/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加快递")
    public Result<Object> addExpress(@RequestBody Express express){
        expressService.addExpress(express);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/express/update",method = RequestMethod.POST)
    @ApiOperation(value = "编辑快递")
    public Result<Object> updateExpress(@RequestBody Express express){
        expressService.updateExpress(express);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/express/del/{ids}",method = RequestMethod.POST)
    @ApiOperation(value = "删除快递")
    public Result<Object> delExpress(@PathVariable int[] ids){
        for(int id:ids){
            expressService.delExpress(id);
        }
        return new ResultUtil<Object>().setData(null);
    }
}
