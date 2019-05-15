package com.mall.manager.service;

import com.baomidou.mybatisplus.service.IService;
import com.mall.manager.model.Vo.ResuhSet;
import com.mall.manager.model.PanelContent;

public interface IContentService extends IService<PanelContent> {
    /**
     * 添加板块内容
     * @param panelContent
     * @return
     */
    int addPanelContent(PanelContent panelContent);

    /**
     * 通过panelId获取板块具体内容
     * @param panelId
     * @return
     */
    ResuhSet getPanelContentListByPanelId(int panelId, int page, int limit);

    /**
     * 删除板块内容
     * @param id
     * @return
     */
    int deletePanelContent(int id);

    /**
     * 编辑板块内容
     * @param panelContent
     * @return
     */
    int updateContent(PanelContent panelContent);

    /**
     * 通过id获取板块内容
     * @param id
     * @return
     */
    PanelContent getPanelContentById(int id);

    /**
     * 获取首页缓存
     * @return
     */
    String getIndexRedis();

    /**
     * 同步首页缓存
     * @return
     */
    int updateIndexRedis();

    /**
     * 获取推荐板块缓存
     * @return
     */
    String getRecommendRedis();

    /**
     * 同步推荐板块缓存
     * @return
     */
    int updateRecommendRedis();
}
