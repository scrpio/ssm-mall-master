package com.mall.manager.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mall.manager.model.Order;
import com.mall.manager.model.Vo.ChartData;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface OrderMapper extends BaseMapper<Order> {
    List<Order> selectByMulti(@Param("search") String search, @Param("orderCol") String orderCol, @Param("orderDir") String orderDir);

    List<ChartData> selectOrderChart(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<ChartData> selectOrderChartByYear(@Param("year") int year);
}