package com.mall.manager.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.manager.service.IItemService;
import com.mall.common.exception.StoreException;
import com.mall.common.jedis.JedisClient;
import com.mall.manager.model.Vo.ItemVo;
import com.mall.manager.model.Vo.ResuhSet;
import com.mall.common.result.ResultCode;
import com.mall.common.util.ToolUtil;
import com.mall.manager.dao.ItemCatMapper;
import com.mall.manager.dao.ItemDescMapper;
import com.mall.manager.dao.ItemMapper;
import com.mall.manager.model.Item;
import com.mall.manager.model.ItemCat;
import com.mall.manager.model.ItemDesc;
import com.mall.manager.model.factory.ModelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;
import javax.jms.JMSException;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements IItemService {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemDescMapper itemDescMapper;
    @Autowired
    private ItemCatMapper itemCatMapper;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource
    private Destination topicDestination;
    @Autowired
    private JedisClient jedisClient;

    @Value("${PRODUCT_ITEM}")
    private String PRODUCT_ITEM;

    @Override
    public ItemVo getItemById(Long itemId) {
        ItemVo itemVo = new ItemVo();
        Item item = itemMapper.selectById(itemId);
        itemVo = ModelFactory.ItemToItemVo(item);
        ItemCat itemCat = itemCatMapper.selectById(itemVo.getCid());
        itemVo.setCname(itemCat.getName());
        ItemDesc itemDesc = itemDescMapper.selectById(itemId);
        itemVo.setDetail(itemDesc.getItemDesc());
        return itemVo;
    }

    @Override
    public Item getNormalItemById(Long id) {
        return itemMapper.selectById(id);
    }

    @Override
    public ResuhSet getItemList(int page, int limit, int cid, String search) {
        ResuhSet result = new ResuhSet();
        //分页执行查询返回结果
        PageHelper.startPage(page, limit);
        List<Item> list = itemMapper.selectItemByCondition(cid, "%" + search + "%");
        PageInfo<Item> pageInfo = new PageInfo<>(list);
        result.setSuccess(true);
        result.setRecordsTotal((int) pageInfo.getTotal());
        result.setData(list);
        return result;
    }

    @Override
    public ResuhSet getItemAllList(int page, int limit) {
        ResuhSet result = new ResuhSet();
        PageHelper.startPage(page, limit);
        //分页执行查询返回结果
        EntityWrapper<Item> entityWrapper = new EntityWrapper<>();
        List<Item> list = itemMapper.selectList(entityWrapper);
        PageInfo<Item> pageInfo = new PageInfo<>(list);
        result.setSuccess(true);
        result.setRecordsTotal((int) pageInfo.getTotal());
        result.setData(list);
        return result;
    }

    @Override
    public ResuhSet getItemSearchList(int draw, int start, int length, int cid, String search, String minDate, String maxDate, String orderCol, String orderDir) {
        ResuhSet result = new ResuhSet();
        //分页执行查询返回结果
        PageHelper.startPage(start / length + 1, length);
        List<Item> list = itemMapper.selectItemByMultiCondition(cid, "%" + search + "%", minDate, maxDate, orderCol, orderDir);
        PageInfo<Item> pageInfo = new PageInfo<>(list);
        result.setRecordsFiltered((int) pageInfo.getTotal());
        result.setRecordsTotal(getAllItemCount());
        result.setDraw(draw);
        result.setData(list);
        return result;
    }

    @Override
    public Integer getAllItemCount() {
        EntityWrapper<Item> entityWrapper = new EntityWrapper<>();
        Integer result = itemMapper.selectCount(entityWrapper);
        if (result == null) {
            throw new StoreException(ResultCode.FAIL);
        }
        return result;
    }

    @Override
    public Item alertItemState(Long id, Integer state) {
        Item item = getNormalItemById(id);
        item.setStatus(state);
        item.setUpdated(new Date());
        if (itemMapper.updateById(item) != 1) {
            throw new StoreException(ResultCode.MODIFY_FAIL);
        }
        return getNormalItemById(id);
    }

    @Override
    public int deleteItem(Long id) {
        if (itemMapper.deleteById(id) != 1) {
            throw new StoreException(ResultCode.MODIFY_FAIL);
        }
        if (itemDescMapper.deleteById(id) != 1) {
            throw new StoreException(ResultCode.MODIFY_FAIL);
        }
        //发送消息同步索引库
        try {
            sendRefreshESMessage("delete", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Item addItem(ItemVo itemVo) {
        long id = ToolUtil.getRandomId();
        Item item = ModelFactory.ItemVoToItem(itemVo);
        item.setId(id);
        item.setStatus(1);
        item.setCreated(new Date());
        item.setUpdated(new Date());
        if (item.getImage().isEmpty()) {
            item.setImage("http://192.168.229.128/images/2018/10/ca8bf9f27ed64ae68fda4a29408fd1c1.png");
        }
        if (itemMapper.insert(item) != 1) {
            throw new StoreException(ResultCode.MODIFY_FAIL);
        }
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(id);
        itemDesc.setItemDesc(itemVo.getDetail());
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());

        if (itemDescMapper.insert(itemDesc) != 1) {
            throw new StoreException(ResultCode.MODIFY_FAIL);
        }
        //发送消息同步索引库
        try {
            sendRefreshESMessage("add", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getNormalItemById(id);
    }

    @Override
    public Item updateItem(Long id, ItemVo itemVo) {
        Item oldItem = getNormalItemById(id);
        Item item = ModelFactory.ItemVoToItem(itemVo);
        if (item.getImage().isEmpty()) {
            item.setImage(oldItem.getImage());
        }
        item.setId(id);
        item.setStatus(oldItem.getStatus());
        item.setCreated(oldItem.getCreated());
        item.setUpdated(new Date());
        if (itemMapper.updateById(item) != 1) {
            throw new StoreException(ResultCode.MODIFY_FAIL);
        }
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(id);
        itemDesc.setItemDesc(itemVo.getDetail());
        itemDesc.setUpdated(new Date());
        itemDesc.setCreated(oldItem.getCreated());
        if (itemDescMapper.updateById(itemDesc) != 1) {
            throw new StoreException(ResultCode.MODIFY_FAIL);
        }
        //同步缓存
        deleteProductDetRedis(id);
        //发送消息同步索引库
        try {
            sendRefreshESMessage("add", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getNormalItemById(id);
    }

    /**
     * 同步商品详情缓存
     *
     * @param id
     */
    public void deleteProductDetRedis(Long id) {
        try {
            jedisClient.del(PRODUCT_ITEM + ":" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息同步索引库
     *
     * @param type
     * @param id
     */
    public void sendRefreshESMessage(String type, Long id) {
        jmsTemplate.send(topicDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(type + "," + String.valueOf(id));
                return textMessage;
            }
        });
    }
}
