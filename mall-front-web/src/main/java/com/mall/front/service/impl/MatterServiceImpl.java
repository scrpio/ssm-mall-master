package com.mall.front.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mall.manager.dao.*;
import com.mall.manager.model.*;
import com.mall.common.jedis.JedisClient;
import com.mall.front.service.IMatterService;
import com.mall.manager.model.Vo.PageResult;
import com.mall.manager.model.Vo.Product;
import com.mall.manager.model.Vo.ProductDet;
import com.mall.manager.model.Vo.ZTreeNode;
import com.mall.manager.model.factory.ModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatterServiceImpl implements IMatterService {
    private final static Logger log = LoggerFactory.getLogger(MatterServiceImpl.class);

    @Autowired
    private BaseSetMapper baseSetMapper;
    @Autowired
    private PanelMapper panelDao;
    @Autowired
    private PanelContentMapper panelContentDao;
    @Autowired
    private ItemMapper itemDao;
    @Autowired
    private ItemDescMapper itemDescDao;
    @Autowired
    private ItemCatMapper itemCatMapper;
    @Autowired
    private JedisClient jedisClient;

    @Value("${PRODUCT_HOME}")
    private String PRODUCT_HOME;

    @Value("${PRODUCT_ITEM}")
    private String PRODUCT_ITEM;

    @Value("${RECOMMEND_PANEL}")
    private String RECOMMEND_PANEL;

    @Override
    public long viewCount() {
        Base base = baseSetMapper.selectById(1);
        base.setViewCount(base.getViewCount() + 1);
        return base.getViewCount();
    }

    private List<Panel> getPanelAndContentsById(int panelId) {
        List<Panel> list = panelDao.selectByPanelId(panelId, 1);
        for (Panel panel : list) {
            List<PanelContent> contentList = panelContentDao.selectListByPanelId(panel.getId());
            for (PanelContent content : contentList) {
                if (content.getProductId() != null) {
                    Item item = itemDao.selectById(content.getProductId());
                    content.setProductName(item.getTitle());
                    content.setSalePrice(item.getPrice());
                    content.setSubTitle(item.getSellPoint());
                }
            }
            panel.setPanelContents(contentList);
        }
        return list;
    }

    @Override
    public List<PanelContent> getNavList() {

        List<PanelContent> list = new ArrayList<>();
        //查询缓存
        try {
            //有缓存则读取
            String json = jedisClient.get("HEADER_PANEL");
            if (json != null) {
                list = new Gson().fromJson(json, new TypeToken<List<PanelContent>>() {
                }.getType());
                log.info("读取了导航栏缓存");
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        EntityWrapper<PanelContent> entityWrapper = new EntityWrapper<>();
        Wrapper<PanelContent> wrapper = entityWrapper.eq("panel_id", 0).orderBy("sort_order");
        list = panelContentDao.selectList(wrapper);
        //把结果添加至缓存
        try {
            jedisClient.set("HEADER_PANEL", new Gson().toJson(list));
            log.info("添加了导航栏缓存");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<Panel> getHome() {
        List<Panel> list = new ArrayList<Panel>();
        try {
            String json = jedisClient.get(PRODUCT_HOME);
            if (json != null) {
                list = new Gson().fromJson(json, new TypeToken<List<Panel>>() {
                }.getType());
                log.info("读取了首页缓存");
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        list = panelDao.selectPanelAll(1, 0);
        for (Panel panel : list) {
            List<PanelContent> contentList = panelContentDao.selectListByPanelId(panel.getId());
            for (PanelContent panelContent : contentList) {
                if (panelContent.getProductId() != null) {
                    Item item = itemDao.selectById(panelContent.getProductId());
                    panelContent.setProductName(item.getTitle());
                    panelContent.setSalePrice(item.getPrice());
                    panelContent.setSubTitle(item.getSellPoint());
                    panelContent.setLimitNum(item.getLimitNum());
                }
            }
            panel.setPanelContents(contentList);
        }
        try {
            jedisClient.set(PRODUCT_HOME, new Gson().toJson(list));
            log.info("添加了首页缓存");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<ZTreeNode> getItemCatList(int parentId) {
        EntityWrapper<ItemCat> entityWrapper = new EntityWrapper<>();
        Wrapper<ItemCat> wrapper = entityWrapper.orderBy("id");
        List<ItemCat> list = itemCatMapper.selectList(wrapper);
        List<ZTreeNode> treeNodeList = new ArrayList<>();
        for (ItemCat itemCat:list){
            ZTreeNode node = ModelFactory.ItemCatToZTreeNode(itemCat);
            treeNodeList.add(node);
        }
        return ZTreeNode.buildList(treeNodeList, 0);
    }

    @Override
    public List<Panel> getRecommendGoods() {
        List<Panel> list = new ArrayList<Panel>();
        try {
            String json = jedisClient.get(RECOMMEND_PANEL);
            if (json != null) {
                list = new Gson().fromJson(json, new TypeToken<List<Panel>>() {
                }.getType());
                log.info("读取了推荐板块缓存");
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        list = getPanelAndContentsById(6);
        try {
            jedisClient.set(RECOMMEND_PANEL, new Gson().toJson(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ProductDet getProductDet(Long id) {
        //查询缓存
        try {
            //有缓存则读取
            String json = jedisClient.get(PRODUCT_ITEM + ":" + id);
            if (json != null) {
                ProductDet productDet = new Gson().fromJson(json, ProductDet.class);
                //重置商品缓存时间
                jedisClient.expire(PRODUCT_ITEM + ":" + id, 604800);
                return productDet;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Item item = itemDao.selectById(id);
        ProductDet productDet = new ProductDet();
        productDet.setProductId(id);
        productDet.setProductName(item.getTitle());
        productDet.setSubTitle(item.getSellPoint());
        if (item.getLimitNum() != null && !item.getLimitNum().toString().isEmpty()) {
            productDet.setLimitNum(Long.valueOf(item.getLimitNum()));
        } else {
            productDet.setLimitNum(Long.valueOf(item.getNum()));
        }
        productDet.setSalePrice(item.getPrice());
        ItemDesc tbItemDesc = itemDescDao.selectById(id);
        productDet.setDetail(tbItemDesc.getItemDesc());
        if (item.getImage() != null && !item.getImage().isEmpty()) {
            String images[] = item.getImage().split(",");
            productDet.setProductImageBig(images[0]);
            List list = new ArrayList();
            for (int i = 0; i < images.length; i++) {
                list.add(images[i]);
            }
            productDet.setProductImageSmall(list);
        }
        //无缓存 把结果添加至缓存
        try {
            jedisClient.set(PRODUCT_ITEM + ":" + id, new Gson().toJson(productDet));
            //设置过期时间
            jedisClient.expire(PRODUCT_ITEM + ":" + id, 604800);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productDet;
    }

    @Override
    public PageResult getAllProduct(int page, int size, String sort, Long cid, int priceGt, int priceLte) {
        PageResult pageResult = new PageResult();
        List<Product> list = new ArrayList<Product>();
        //分页执行查询返回结果
        if (page <= 0) {
            page = 1;
        }
        PageHelper.startPage(page, size);
        //判断条件
        String orderCol = "created";
        String orderDir = "desc";
        if (sort.equals("1")) {
            orderCol = "price";
            orderDir = "asc";
        } else if (sort.equals("-1")) {
            orderCol = "price";
            orderDir = "desc";
        } else {
            orderCol = "created";
            orderDir = "desc";
        }
        List<Item> itemList = itemDao.selectItemFront(cid, orderCol, orderDir, priceGt, priceLte);
        PageInfo<Item> pageInfo = new PageInfo<Item>(itemList);
        for (Item item : itemList) {
            Product product = ModelFactory.ItemToProduct(item);
            list.add(product);
        }
        pageResult.setData(list);
        pageResult.setTotal((int) pageInfo.getTotal());
        return pageResult;
    }
}
