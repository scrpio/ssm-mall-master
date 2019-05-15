package com.mall.manager.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.mall.manager.dao.*;
import com.mall.manager.service.ChartService;
import com.mall.common.util.TimeUtil;
import com.mall.manager.model.ItemCat;
import com.mall.manager.model.Vo.ChartData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class ChartServiceImpl implements ChartService {
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private ItemCatMapper itemCatMapper;
    @Autowired
    private MemberMapper memberMapper;

    /**
     * 用户注册图表数据
     *
     * @param year
     * @return
     */
    @Override
    public Map<String, Object> barChartData(Date startTime, Date endTime) {
        Map<String, Object> result = new HashMap<>();
        List<Object> xData = xDataFull(startTime, endTime);
        List<Object> members = yDataFull(memberMapper.selectMemberChart(startTime, endTime), xData);
        result.put("xData", xData);
        result.put("yData", members);
        return result;
    }

    /**
     * 销售分类统计饼状图表数据
     *
     * @param year
     * @return
     */
    @Override
    public Map<String, Object> orderPieData(Date startTime, Date endTime) {
        Map<String, Object> result = new HashMap<>();
        List<Object> legen = getLegendData();
        result.put("seriesData", orderItemMapper.selectChartPie(startTime, endTime));
        result.put("legendData", legen);
        return result;
    }

    /**
     * 销售额图表数据
     *
     * @param type
     * @param startTime
     * @param endTime
     * @param year
     * @return
     */
    @Override
    public Map<String, Object> orderChartData(int type, int year, Date startTime, Date endTime) {
        Map<String, Object> fullData = new HashMap<>();
        if (type == 0) {
            List<ChartData> data = orderMapper.selectOrderChart(startTime, endTime);
            List<Object> xData = xDataFull(startTime, endTime);
            List<Object> yData = yDataFull(data, xData);
            fullData.put("xData", xData);
            fullData.put("yData", yData);
        } else if (type == 1) {
            List<ChartData> data = orderMapper.selectOrderChartByYear(year);
            List<Object> xData = xDataFullYear(year);
            List<Object> yData = yDataFull(data, xData);
            fullData.put("xData", xData);
            fullData.put("yData", yData);
        }
        return fullData;
    }

    /**
     * 销售分类统计饼状图表数据
     *
     * @param year
     * @return
     */
    @Override
    public Map<String, Object> mixChartData(int year) {
        Map<String, Object> result = new HashMap<>();
        List<Object> xData = xDataFullYear(year);
        List<Object> yData = yDataFull(orderMapper.selectOrderChartByYear(year), xData);
        List<Object> listFamily = yDataFull(orderItemMapper.selectFamilyData(year), xData);
        List<Object> listFood = yDataFull(orderItemMapper.selectFoodData(year), xData);
        List<Object> listCosmetic = yDataFull(orderItemMapper.selectCosmeticData(year), xData);
        List<Object> listApparel = yDataFull(orderItemMapper.selectApparelData(year), xData);
        List<Object> listDigital = yDataFull(orderItemMapper.selectDigitalData(year), xData);
        List<Object> listOther = yDataFull(orderItemMapper.selectOtherData(year), xData);
        result.put("xData", xData);
        result.put("yData", yData);
        result.put("family", listFamily);
        result.put("food", listFood);
        result.put("cosmetic", listCosmetic);
        result.put("apparel", listApparel);
        result.put("digital", listDigital);
        result.put("other", listOther);
        return result;
    }

    @Override
    public List<Object> shareChartData(int year) {
        List<Object> list = new ArrayList<>();
        List<Object> xData = xDataFullYear(year);
        List<Object> yData = yDataFull(orderMapper.selectOrderChartByYear(year), xData);
        List<Object> listFamily = yDataFull(orderItemMapper.selectFamilyData(year), xData);
        List<Object> listFood = yDataFull(orderItemMapper.selectFoodData(year), xData);
        List<Object> listCosmetic = yDataFull(orderItemMapper.selectCosmeticData(year), xData);
        List<Object> listApparel = yDataFull(orderItemMapper.selectApparelData(year), xData);
        List<Object> listDigital = yDataFull(orderItemMapper.selectDigitalData(year), xData);
        List<Object> listOther = yDataFull(orderItemMapper.selectOtherData(year), xData);
        xData.add(0, "日期");
        listFamily.add(0, "家用电器");
        listFood.add(0, "食用酒水");
        listCosmetic.add(0, "个护健康");
        listApparel.add(0, "服饰箱包");
        listDigital.add(0, "手机数码");
        listOther.add(0, "其他");
        list.add(xData);
        list.add(listFamily);
        list.add(listFood);
        list.add(listCosmetic);
        list.add(listApparel);
        list.add(listDigital);
        list.add(listOther);
        return list;
    }

    /**
     * x轴无数据补0
     *
     * @param startTime
     * @param endTime
     * @return
     */
    private List<Object> xDataFull(Date startTime, Date endTime) {
        List<Object> xData = new ArrayList<>();
        //相差
        long betweenDay = DateUtil.between(startTime, endTime, DateUnit.DAY);
        //起始时间
        Date everyday = startTime;
        for (int i = 0; i <= betweenDay; i++) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = formatter.format(everyday);
            xData.add(dateString);
            //时间+1天
            Calendar cal = Calendar.getInstance();
            cal.setTime(everyday);
            cal.add(Calendar.DAY_OF_MONTH, 1);
            everyday = cal.getTime();
        }
        return xData;
    }

    /**
     * x轴无数据补0
     *
     * @param year
     * @return
     */
    private List<Object> xDataFullYear(int year) {
        List<Object> xData = new ArrayList<>();
        //起始月份
        Date everyMonth = TimeUtil.getBeginDayOfYear(year);
        for (int i = 0; i < 12; i++) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
            String dateString = formatter.format(everyMonth);
            xData.add(dateString);
            //时间+1月
            Calendar cal = Calendar.getInstance();
            cal.setTime(everyMonth);
            cal.add(Calendar.MONTH, 1);
            everyMonth = cal.getTime();
        }
        return xData;
    }

    /**
     * y轴无数据补0
     *
     * @param data
     * @param xData
     * @return
     */
    private List<Object> yDataFull(List<ChartData> data, List<Object> xData) {
        List<Object> yData = new ArrayList<>();
        int count = -1;
        for (int i = 0; i < xData.size(); i++) {
            boolean flag = true;
            for (ChartData chartData : data) {
                if (chartData.getDate().equals(xData.get(i))) {
                    //有数据
                    flag = false;
                    count++;
                    break;
                }
            }
            if (!flag) {
                yData.add(data.get(count).getTotal());
            } else {
                yData.add(0);
            }
        }
        return yData;
    }

    /**
     * 商品分类数组
     *
     * @return
     */
    private List<Object> getLegendData() {
        EntityWrapper<ItemCat> entityWrapper = new EntityWrapper<>();
        Wrapper<ItemCat> wrapper = entityWrapper.setSqlSelect("name").eq("parent_id", 0);
        List<Object> legend = itemCatMapper.selectObjs(wrapper);
        return legend;
    }
}
