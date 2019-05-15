package com.mall.manager.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mall.manager.model.PanelContent;

import java.util.List;

public interface PanelContentMapper extends BaseMapper<PanelContent> {
    List<PanelContent> selectListByPanelId(int panelId);
}