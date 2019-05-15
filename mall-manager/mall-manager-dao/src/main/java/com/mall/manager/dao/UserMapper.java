package com.mall.manager.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mall.manager.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface UserMapper extends BaseMapper<User> {
    Set<String> getRoles(@Param("username") String username);

    Set<String> getPermissions(@Param("username") String username);

    List<User> selectByUserInfo(@Param("condition") String condition);
}