package com.mall.manager.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mall.manager.model.Panel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PanelMapper extends BaseMapper<Panel> {
    List<Panel> selectByPanelId(@Param("id") int id, @Param("status") int status);

    List<Panel> selectPanelAll(@Param("status") int status, @Param("position") int position);
}