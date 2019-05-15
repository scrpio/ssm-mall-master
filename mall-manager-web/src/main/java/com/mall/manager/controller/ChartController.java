package com.mall.manager.controller;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.mall.common.result.Result;
import com.mall.common.result.ResultUtil;
import com.mall.manager.service.ChartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@Api(description = "图表数据")
public class ChartController {
    @Autowired
    private ChartService chartService;

    @RequestMapping(value = "/chart/order", method = RequestMethod.GET)
    @ApiOperation(value = "销售额图表数据")
    public Result<Object> getOrderChart(@RequestParam int type,
                                        @RequestParam(required = false) int year,
                                        @RequestParam(required = false) String startTime,
                                        @RequestParam(required = false) String endTime) {
        Date startDate = DateUtil.parse(startTime);
        Date endDate = DateUtil.parse(endTime);
        if (type == -1) {
            long betweenDay = DateUtil.between(DateUtil.beginOfDay(startDate), DateUtil.endOfDay(endDate), DateUnit.DAY);
            if (betweenDay > 30) {
                return new ResultUtil<Object>().setErrorMsg("所选日期范围过长，最多不能超过31天");
            }
        }
        Map<String, Object> result = chartService.orderChartData(type, year, startDate, endDate);
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/chart/orderPie", method = RequestMethod.GET)
    @ApiOperation(value = "销售分类图表数据")
    public Result<Map<String, Object>> getOrderChartPie(@RequestParam(required = false) String startTime,
                                                        @RequestParam(required = false) String endTime) {
        Date startDate = DateUtil.beginOfDay(DateUtil.parse(startTime));
        Date endDate = DateUtil.endOfDay(DateUtil.parse(endTime));
        Map<String, Object> result = chartService.orderPieData(startDate, endDate);
        return new ResultUtil<Map<String, Object>>().setData(result);
    }

    @RequestMapping(value = "/chart/bar", method = RequestMethod.GET)
    @ApiOperation(value = "注册用户图表数据")
    public Result<Map<String, Object>> getItemChart(@RequestParam(required = false) String startTime,
                                                    @RequestParam(required = false) String endTime) {
        Date startDate = DateUtil.parse(startTime);
        Date endDate = DateUtil.parse(endTime);
        long betweenDay = DateUtil.between(DateUtil.beginOfDay(startDate), DateUtil.endOfDay(endDate), DateUnit.DAY);
        if (betweenDay > 7) {
            return new ResultUtil<Map<String, Object>>().setErrorMsg("所选日期范围过长，最多不能超过7天");
        }
        Map<String, Object> result = chartService.barChartData(startDate, endDate);
        return new ResultUtil<Map<String, Object>>().setData(result);
    }

    @RequestMapping(value = "/chart/mix",method = RequestMethod.GET)
    @ApiOperation(value = "商品类别图表数据")
    public Result<Map<String,Object>> getCatChart(@RequestParam int year) {
        Map<String,Object> result = chartService.mixChartData(year);
        return new ResultUtil<Map<String,Object>>().setData(result);
    }

    @RequestMapping(value = "/chart/share",method = RequestMethod.GET)
    @ApiOperation(value = "商品类别图表数据")
    public Result<List<Object>> getShareChart(@RequestParam int year) {
        List<Object> result = chartService.shareChartData(year);
        return new ResultUtil<List<Object>>().setData(result);
    }
}
