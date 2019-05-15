package com.mall.manager.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mall.manager.model.RolePerm;
import org.apache.ibatis.annotations.Param;

public interface RolePermMapper extends BaseMapper<RolePerm> {
    String getPermsIdByRoleId(@Param("roleId") int roleId);

}