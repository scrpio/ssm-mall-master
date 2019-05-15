package com.mall.manager.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mall.manager.model.SysLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysLogMapper extends BaseMapper<SysLog> {
    List<SysLog> selectByMulti(@Param("search") String search, @Param("orderCol") String orderCol, @Param("orderDir") String orderDir);

}