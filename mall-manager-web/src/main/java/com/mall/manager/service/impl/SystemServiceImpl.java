package com.mall.manager.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.manager.service.ISystemService;
import com.mall.common.exception.StoreException;
import com.mall.manager.model.Vo.ResuhSet;
import com.mall.common.result.ResultCode;
import com.mall.manager.dao.BaseSetMapper;
import com.mall.manager.dao.OrderItemMapper;
import com.mall.manager.dao.SysLogMapper;
import com.mall.manager.model.Base;
import com.mall.manager.model.OrderItem;
import com.mall.manager.model.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemServiceImpl extends ServiceImpl<SysLogMapper,SysLog> implements ISystemService {
    @Autowired
    private BaseSetMapper baseSetMapper;
    @Autowired
    private SysLogMapper sysLogMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public Base getBase() {
        Base tbBase = baseSetMapper.selectById(1);
        if (tbBase == null) {
            throw new StoreException(ResultCode.FAIL);
        }
        return tbBase;
    }

    @Override
    public int updateBase(Base base) {
        if (baseSetMapper.updateById(base) != 1) {
            throw new StoreException(ResultCode.FAIL);
        }
        return 1;
    }

    @Override
    public OrderItem getWeekHot() {
        List<OrderItem> list = orderItemMapper.getWeekHot();
        if (list == null) {
            throw new StoreException(ResultCode.FAIL);
        }
        if (list.size() == 0) {
            OrderItem orderItem = new OrderItem();
            orderItem.setTotal(0);
            orderItem.setTitle("暂无数据");
            orderItem.setPicPath("");
            return orderItem;
        }
        return list.get(0);
    }

    @Override
    public int addLog(SysLog sysLog) {
        if (sysLogMapper.insert(sysLog) != 1) {
            throw new StoreException(ResultCode.FAIL);
        }
        return 1;
    }

    @Override
    public ResuhSet getLogList(int page, int limit) {
        ResuhSet result = new ResuhSet();
        PageHelper.startPage(page, limit);
        EntityWrapper<SysLog> entityWrapper = new EntityWrapper<>();
        List<SysLog> list = sysLogMapper.selectList(entityWrapper);
        PageInfo<SysLog> pageInfo = new PageInfo<>(list);
        result.setSuccess(true);
        result.setRecordsTotal((int) pageInfo.getTotal());
        result.setData(list);
        return result;
    }

    @Override
    public Long countLog() {
        EntityWrapper<SysLog> entityWrapper = new EntityWrapper<>();
        Long result = (long) sysLogMapper.selectCount(entityWrapper);
        if (result == null) {
            throw new StoreException(ResultCode.FAIL);
        }
        return result;
    }

    @Override
    public int deleteLog(int id) {
        if (sysLogMapper.deleteById(id) != 1) {
            throw new StoreException(ResultCode.FAIL);
        }
        return 1;
    }
}
