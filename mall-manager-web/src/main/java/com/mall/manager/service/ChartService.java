package com.mall.manager.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ChartService {
    Map<String,Object> barChartData(Date startTime, Date endTime);

    Map<String, Object> orderPieData(Date startTime, Date endTime);

    Map<String, Object> orderChartData(int type, int year, Date startTime, Date endTime);

    Map<String, Object> mixChartData(int year);

    List<Object> shareChartData(int year);
}
