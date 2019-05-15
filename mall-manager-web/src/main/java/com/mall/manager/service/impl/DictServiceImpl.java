package com.mall.manager.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mall.manager.service.IDictService;
import com.mall.common.constant.CommonConstant;
import com.mall.manager.dao.DictMapper;
import com.mall.manager.model.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper,Dict> implements IDictService {
    @Autowired
    private DictMapper dictMapper;

    @Override
    public List<Dict> getDictList() {
        EntityWrapper<Dict> entityWrapper = new EntityWrapper<>();
        Wrapper<Dict> wrapper = entityWrapper.eq("type",CommonConstant.DICT_EXT);
        List<Dict> list = dictMapper.selectList(wrapper);
        return list;
    }

    @Override
    public List<Dict> getStopList() {
        EntityWrapper<Dict> entityWrapper = new EntityWrapper<>();
        Wrapper<Dict> wrapper = entityWrapper.eq("type",CommonConstant.DICT_STOP);
        List<Dict> list = dictMapper.selectList(wrapper);
        return list;
    }

    @Override
    public int addDict(Dict dict) {
        dictMapper.insert(dict);
        return 1;
    }

    @Override
    public int updateDict(Dict dict) {
        dictMapper.updateById(dict);
        return 1;
    }

    @Override
    public int delDict(int id) {
        dictMapper.deleteById(id);
        return 1;
    }
}
