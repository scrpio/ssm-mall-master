package com.mall.manager.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.manager.service.IContentService;
import com.mall.common.exception.StoreException;
import com.mall.common.jedis.JedisClient;
import com.mall.manager.model.Vo.ResuhSet;
import com.mall.common.result.ResultCode;
import com.mall.manager.dao.ItemMapper;
import com.mall.manager.dao.PanelContentMapper;
import com.mall.manager.model.Item;
import com.mall.manager.model.PanelContent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl extends ServiceImpl<PanelContentMapper, PanelContent> implements IContentService {

    @Autowired
    private PanelContentMapper panelContentMapper;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private JedisClient jedisClient;

    @Value("${PRODUCT_HOME}")
    private String PRODUCT_HOME;

    @Value("${PRODUCT_ITEM}")
    private String PRODUCT_ITEM;

    @Value("${RECOMMEND_PANEL}")
    private String RECOMMEND_PANEL;

    /**
     * 同步首页缓存
     */
    private void deleteHomeRedis() {
        try {
            jedisClient.del(PRODUCT_HOME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int addPanelContent(PanelContent panelContent) {
        panelContent.setCreated(new Date());
        panelContent.setUpdated(new Date());
        if (panelContentMapper.insert(panelContent) != 1) {
            throw new StoreException(ResultCode.FAIL);
        }
        deleteHomeRedis();
        return 1;
    }

    @Override
    public ResuhSet getPanelContentListByPanelId(int panelId, int page, int limit) {
        ResuhSet resuhSet = new ResuhSet();
        PageHelper.startPage(page, limit);
        List<PanelContent> list = panelContentMapper.selectListByPanelId(panelId);
        PageInfo<PanelContent> pageInfo = new PageInfo<>(list);
        for (PanelContent content : list) {
            if (content.getProductId() != null) {
                Item item = itemMapper.selectById(content.getProductId());
                content.setProductName(item.getTitle());
                content.setSalePrice(item.getPrice());
                content.setSubTitle(item.getSellPoint());
            }
        }
        resuhSet.setRecordsTotal((int) pageInfo.getTotal());
        resuhSet.setSuccess(true);
        resuhSet.setData(list);
        return resuhSet;
    }

    @Override
    public int deletePanelContent(int id) {
        if (panelContentMapper.deleteById(id) != 1) {
            throw new StoreException(ResultCode.FAIL);
        }
        deleteHomeRedis();
        return 1;
    }

    @Override
    public int updateContent(PanelContent panelContent) {
        PanelContent old = getPanelContentById(panelContent.getId());
        if (StringUtils.isBlank(panelContent.getPicUrl())) {
            panelContent.setPicUrl(old.getPicUrl());
        }
        if (StringUtils.isBlank(panelContent.getPicUrl2())) {
            panelContent.setPicUrl2(old.getPicUrl2());
        }
        if (StringUtils.isBlank(panelContent.getPicUrl3())) {
            panelContent.setPicUrl3(old.getPicUrl3());
        }
        panelContent.setCreated(old.getCreated());
        panelContent.setUpdated(new Date());
        if (panelContentMapper.updateById(panelContent) != 1) {
            throw new StoreException(ResultCode.FAIL);
        }
        //同步缓存
        deleteHomeRedis();
        return 1;
    }

    @Override
    public PanelContent getPanelContentById(int id) {
        PanelContent panelContent = panelContentMapper.selectById(id);
        if (panelContent == null) {
            throw new StoreException(ResultCode.FAIL);
        }
        return panelContent;
    }

    @Override
    public String getIndexRedis() {
        try {
            String json = jedisClient.get(PRODUCT_HOME);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public int updateIndexRedis() {
        deleteHomeRedis();
        return 1;
    }

    @Override
    public String getRecommendRedis() {
        try {
            String json = jedisClient.get(RECOMMEND_PANEL);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public int updateRecommendRedis() {
        try {
            jedisClient.del(RECOMMEND_PANEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }
}
