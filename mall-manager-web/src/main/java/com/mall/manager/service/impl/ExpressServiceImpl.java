package com.mall.manager.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.manager.dao.ExpressMapper;
import com.mall.manager.model.Express;
import com.mall.manager.model.Vo.ResuhSet;
import com.mall.manager.service.IExpressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ExpressServiceImpl extends ServiceImpl<ExpressMapper,Express> implements IExpressService {
    @Autowired
    private ExpressMapper expressMapper;

    @Override
    public ResuhSet getExpressList(int page, int limit) {
        ResuhSet result = new ResuhSet();
        PageHelper.startPage(page, limit);
        EntityWrapper<Express> entityWrapper = new EntityWrapper<>();
        Wrapper<Express> wrapper = entityWrapper.orderBy("sort_order",true);
        List<Express> list = expressMapper.selectList(wrapper);
        PageInfo<Express> pageInfo = new PageInfo<>(list);
        result.setSuccess(true);
        result.setRecordsTotal((int) pageInfo.getTotal());
        result.setData(list);
        return result;
    }

    @Override
    public int addExpress(Express express) {
        express.setCreated(new Date());
        expressMapper.insert(express);
        return 1;
    }

    @Override
    public int updateExpress(Express express) {
        express.setUpdated(new Date());
        expressMapper.updateById(express);
        return 1;
    }

    @Override
    public int delExpress(int id) {
        expressMapper.deleteById(id);
        return 1;
    }
}
