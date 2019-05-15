package com.mall.manager.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mall.manager.service.IPanelService;
import com.mall.common.exception.StoreException;
import com.mall.common.jedis.JedisClient;
import com.mall.manager.dao.ItemMapper;
import com.mall.manager.dao.PanelContentMapper;
import com.mall.manager.model.Vo.ZTreeNode;
import com.mall.manager.dao.PanelMapper;
import com.mall.manager.model.Panel;
import com.mall.manager.model.factory.ModelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PanelServiceImpl extends ServiceImpl<PanelMapper,Panel> implements IPanelService {

    @Autowired
    private PanelMapper panelMapper;
    @Autowired
    private PanelContentMapper panelContentMapper;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private JedisClient jedisClient;

    @Value("${PRODUCT_HOME}")
    private String PRODUCT_HOME;

    @Value("${RECOMMEND_PANEL}")
    private String RECOMMEND_PANEL;

    @Override
    public Panel getPanelById(int id) {
        Panel panel = panelMapper.selectById(id);
        if (panel == null) {
            throw new StoreException("通过id获得板块失败");
        }
        return panel;
    }

    @Override
    public List<ZTreeNode> getPanelList(int position, boolean showAll) {
        EntityWrapper<Panel> entityWrapper = new EntityWrapper<>();
        List<ZTreeNode> list = new ArrayList<>();
        List<Panel> panelList;
        if (position == 0 && !showAll) {
            //除去非轮播
            Wrapper<Panel> wrapper = entityWrapper.ne("type", 0).and().eq("position", position).orderBy("sort_order");
            panelList = panelMapper.selectList(wrapper);
            for (Panel panel : panelList) {
                ZTreeNode zTreeNode = ModelFactory.PanelToZTreeNode(panel);
                list.add(zTreeNode);
            }
        } else if (position == -1 && showAll) {
            //仅含轮播
            Wrapper<Panel> wrapper = entityWrapper.eq("type", 0).and().eq("position", 0).orderBy("sort_order");
            panelList = panelMapper.selectList(wrapper);
            for (Panel panel : panelList) {
                ZTreeNode zTreeNode = ModelFactory.PanelToZTreeNode(panel);
                list.add(zTreeNode);
            }
        } else if (position != 0 && !showAll) {
            //其他版块
            Wrapper<Panel> wrapper = entityWrapper.eq("position", position).orderBy("sort_order");
            panelList = panelMapper.selectList(wrapper);
            for (Panel panel : panelList) {
                ZTreeNode zTreeNode = ModelFactory.PanelToZTreeNode(panel);
                list.add(zTreeNode);
            }
        } else {
            //所有版块
            Wrapper<Panel> wrapper = entityWrapper.orderBy("sort_order");
            panelList = panelMapper.selectList(wrapper);
            for (Panel panel : panelList) {
                ZTreeNode zTreeNode = ModelFactory.PanelToZTreeNode(panel);
                list.add(zTreeNode);
            }
        }

        return list;
    }

    @Override
    public int addPanel(Panel panel) {
        if (panel.getType() == 0) {
            EntityWrapper<Panel> entityWrapper = new EntityWrapper<>();
            Wrapper<Panel> wrapper = entityWrapper.eq("type", 0);
            List<Panel> list = panelMapper.selectList(wrapper);
            if (list != null && list.size() > 0) {
                throw new StoreException("已有轮播图板块,轮播图仅能添加1个!");
            }
        }
        panel.setCreated(new Date());
        panel.setUpdated(new Date());

        if (panelMapper.insert(panel) != 1) {
            throw new StoreException("添加板块失败");
        }
        //同步缓存
        deleteHomeRedis();
        return 1;
    }

    @Override
    public int updatePanel(Panel panel) {
        Panel old = getPanelById(panel.getId());
        panel.setUpdated(new Date());
        if (panelMapper.updateById(panel) != 1) {
            throw new StoreException("更新板块失败");
        }
        //同步缓存
        deleteHomeRedis();
        return 1;
    }

    @Override
    public int deletePanel(int id) {
        if (panelMapper.deleteById(id) != 1) {
            throw new StoreException("删除内容分类失败");
        }
        //同步缓存
        deleteHomeRedis();
        return 1;
    }

    /**
     * 同步首页缓存
     */
    public void deleteHomeRedis() {
        try {
            jedisClient.del(PRODUCT_HOME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
