package com.mall.manager.service;

import com.baomidou.mybatisplus.service.IService;
import com.mall.manager.model.Vo.ZTreeNode;
import com.mall.manager.model.Panel;

import java.util.List;

public interface IPanelService extends IService<Panel> {
    /**
     * 通过id获取板块
     * @param id
     * @return
     */
    Panel getPanelById(int id);

    /**
     * 获取板块类目
     * @param position
     * @param showAll
     * @return
     */
    List<ZTreeNode> getPanelList(int position, boolean showAll);

    /**
     * 添加板块
     * @param tbPanel
     * @return
     */
    int addPanel(Panel tbPanel);

    /**
     * 更新板块
     * @param tbPanel
     * @return
     */
    int updatePanel(Panel tbPanel);

    /**
     * 删除板块
     * @param id
     * @return
     */
    int deletePanel(int id);
}
