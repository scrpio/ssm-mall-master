package com.mall.manager.controller;

import com.mall.manager.model.Vo.ResuhSet;
import com.mall.manager.service.ISystemService;
import com.mall.common.result.Result;
import com.mall.common.result.ResultUtil;
import com.mall.common.util.IPInfoUtil;
import com.mall.manager.model.Base;
import com.mall.manager.model.OrderItem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(description = "系统管理")
public class SystemController {
    @Autowired
    private ISystemService systemService;

    @RequestMapping(value = "/sys/base", method = RequestMethod.GET)
    @ApiOperation(value = "获取基本设置")
    public Result<Base> getBase() {
        Base tbBase = systemService.getBase();
        return new ResultUtil<Base>().setData(tbBase);
    }

    @RequestMapping(value = "/sys/base/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑基本设置")
    public Result<Object> updateBase(@RequestBody Base tbBase) {
        systemService.updateBase(tbBase);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/sys/weekHot", method = RequestMethod.GET)
    @ApiOperation(value = "获取本周热销商品数据")
    public Result<OrderItem> getWeekHot() {
        OrderItem tbOrderItem = systemService.getWeekHot();
        return new ResultUtil<OrderItem>().setData(tbOrderItem);
    }

    @RequestMapping(value = "/sys/weather", method = RequestMethod.GET)
    @ApiOperation(value = "获取天气信息")
    public Result<Object> getWeather(HttpServletRequest request) {

        String result = IPInfoUtil.getIpInfo(IPInfoUtil.getIpAddr(request));
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/sys/log", method = RequestMethod.GET)
    public ResuhSet getLog(@RequestParam int page, @RequestParam int limit) {
        ResuhSet result = systemService.getLogList(page, limit);
        return result;
    }

    @RequestMapping(value = "/sys/log/del/{ids}", method = RequestMethod.POST)
    public Result<Object> delLog(@PathVariable int[] ids) {
        for (int id : ids) {
            systemService.deleteLog(id);
        }
        return new ResultUtil<Object>().setData(null);
    }
}
