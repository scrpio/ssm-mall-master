package com.mall.manager.service;

import com.baomidou.mybatisplus.service.IService;
import com.mall.manager.model.Vo.ResuhSet;
import com.mall.manager.model.Permission;
import com.mall.manager.model.Role;
import com.mall.manager.model.User;
import com.mall.manager.model.Vo.Router;
import com.mall.manager.model.Vo.UserVo;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;
import java.util.Set;

public interface IUserService extends IService<User> {
    /**
     * 通过用户名获取用户
     *
     * @param username
     * @return
     */
    User getUserByUsername(String username);

    /**
     * 通过用户名获取角色
     *
     * @param username
     * @return
     */
    Set<String> getRoles(String username);

    /**
     * 通过用户名获取权限
     *
     * @param username
     * @return
     */
    Set<String> getPermissions(String username);

    /**
     * 获取用户列表
     *
     * @return
     */
    ResuhSet getUserList(int page, int limit, String condition);

    /**
     * 添加管理员
     *
     * @param user
     * @return
     */
    int addUser(User user);

    /**
     * 更新用户
     *
     * @param user
     * @return
     */
    int updateUser(User user);

    /**
     * 更改状态
     *
     * @param userVo
     * @return
     */
    int changeUserState(UserVo userVo);

    /**
     * 修改密码
     *
     * @param userVo
     * @return
     */
    int changePassword(UserVo userVo);

    /**
     * 删除管理员
     *
     * @param userId
     * @return
     */
    int deleteUser(Long userId);

    /**
     * 获取角色列表
     *
     * @param page
     * @param limit
     * @param condition
     * @return
     */
    ResuhSet getRoleList(int page, int limit, String condition);

    /**
     * 获取所有角色
     *
     * @return
     */
    List<Role> getAllRoles();

    /**
     * 通过角色id获得角色
     *
     * @param id
     * @return
     */
    Role getRoleById(int id);

    /**
     * 添加角色
     *
     * @param role
     * @return
     */
    int addRole(Role role);

    /**
     * 更新角色
     *
     * @param role
     * @return
     */
    int updateRole(Role role);

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    int deleteRole(int id);

    /**
     * 获得权限列表
     *
     * @return
     */
    ResuhSet getPermissionList();

    /**
     * 通过用户userId获得树状菜单
     *
     * @param userId
     * @return
     */
    List<Router> routersByUserId(Long userId);

    /**
     * 通过角色roleId取得权限PermissionIds
     *
     * @param roleId
     * @return
     */
    int[] permIdsByRoleId(int roleId);

    /**
     * 通过用户userId获得菜单权限
     *
     * @param userId
     * @return
     */
    List<String> permsByUserId(Long userId);

    /**
     * 添加权限
     *
     * @param permission
     * @return
     */
    int addPermission(Permission permission);

    /**
     * 更新权限
     *
     * @param permission
     * @return
     */
    int updatePermission(Permission permission);

    /**
     * 删除权限
     *
     * @param id
     * @return
     */
    int deletePermission(Long id);

    /**
     * 清除权限缓存
     *
     * @param userId
     * @return
     */
    @CacheEvict(value = "permission", key = "#userId")
    boolean clearCache(Long userId);

}
