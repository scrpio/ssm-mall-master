package com.mall.manager.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mall.manager.model.Item;
import com.mall.manager.model.Vo.ChartData;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ItemMapper extends BaseMapper<Item> {
    Item selectById(Long productId);

    List<Item> selectItemFront(@Param("cid") Long cid, @Param("orderCol") String orderCol, @Param("orderDir") String orderDir,
                               @Param("priceGt") int priceGt, @Param("priceLte") int priceLte);

    List<Item> selectItemByCondition(@Param("cid") int cid, @Param("search") String search);

    List<Item> selectItemByMultiCondition(@Param("cid") int cid, @Param("search") String search, @Param("minDate") String minDate,
                                          @Param("maxDate") String maxDate, @Param("orderCol") String orderCol,
                                          @Param("orderDir") String orderDir);

    List<ChartData> selectItemChart(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<ChartData> selectItemChartByYear(@Param("year") int year);
}