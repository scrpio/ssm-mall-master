package com.mall.manager.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mall.manager.model.OrderItem;
import com.mall.manager.model.Vo.ChartData;
import com.mall.manager.model.Vo.ChartPie;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface OrderItemMapper extends BaseMapper<OrderItem> {
    List<OrderItem> getWeekHot();

    List<ChartPie> selectChartPie(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<ChartData> selectFamilyData(@Param("year") int year);

    List<ChartData> selectFoodData(@Param("year") int year);

    List<ChartData> selectCosmeticData(@Param("year") int year);

    List<ChartData> selectApparelData(@Param("year") int year);

    List<ChartData> selectDigitalData(@Param("year") int year);

    List<ChartData> selectOtherData(@Param("year") int year);
}