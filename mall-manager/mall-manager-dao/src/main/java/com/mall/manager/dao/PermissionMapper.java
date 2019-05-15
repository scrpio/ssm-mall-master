package com.mall.manager.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mall.manager.model.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission> {
    List<Permission> listPermissionUserId(@Param("userId") long userId);
}