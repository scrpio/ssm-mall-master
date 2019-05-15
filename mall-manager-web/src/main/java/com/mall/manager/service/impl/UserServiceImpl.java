package com.mall.manager.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mall.manager.dao.PermissionMapper;
import com.mall.manager.dao.RoleMapper;
import com.mall.manager.dao.RolePermMapper;
import com.mall.manager.dao.UserMapper;
import com.mall.manager.service.IUserService;
import com.mall.common.exception.StoreException;
import com.mall.common.util.ToolUtil;
import com.mall.manager.model.Vo.ResuhSet;
import com.mall.manager.model.Vo.RoleVo;
import com.mall.manager.model.Permission;
import com.mall.manager.model.Role;
import com.mall.manager.model.RolePerm;
import com.mall.manager.model.User;
import com.mall.manager.model.Vo.Router;
import com.mall.manager.model.Vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.*;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RolePermMapper rolePermMapper;

    @Override
    public User getUserByUsername(String username) {
        EntityWrapper<User> entityWrapper = new EntityWrapper<>();
        Wrapper<User> wrapper = entityWrapper.eq("username", username).and().eq("state", 1);
        List<User> list = userMapper.selectList(wrapper);
        if (!list.isEmpty()) {
            Role role = getRoleById(list.get(0).getRoleId());
            list.get(0).setRoleNames(role.getName());
            return list.get(0);
        }
        return null;
    }

    @Override
    public Set<String> getRoles(String username) {
        return userMapper.getRoles(username);
    }

    @Override
    public Set<String> getPermissions(String username) {
        return userMapper.getPermissions(username);
    }

    @Override
    public ResuhSet getUserList(int page, int limit, String condition) {
        ResuhSet result = new ResuhSet();
        PageHelper.startPage(page, limit);
        EntityWrapper<User> entityWrapper = new EntityWrapper<>();
        Wrapper<User> wrapper= entityWrapper.like("username",condition).or().like("phone",condition).or().like("email",condition);
        List<User> list = userMapper.selectList(wrapper);
        PageInfo<User> pageInfo = new PageInfo<>(list);
        if (list.size() == 0) {
            throw new StoreException("暂无数据");
        }
        for (User user : list) {
            String names = "";
            Iterator it = getRoles(user.getUsername()).iterator();
            while (it.hasNext()) {
                names += it.next() + " ";
            }
            user.setPassword("");
            user.setRoleNames(names);
        }
        result.setRecordsTotal((int) pageInfo.getTotal());
        result.setSuccess(true);
        result.setData(list);
        return result;
    }

    @Override
    public int addUser(User user) {
        if (!getUserByName(user.getUsername())) {
            throw new StoreException("用户名已存在");
        }
        if (!getUserByPhone(user.getPhone())) {
            throw new StoreException("手机号已存在");
        }
        if (!getUserByEmail(user.getEmail())) {
            throw new StoreException("邮箱已存在");
        }
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);
        user.setState(true);
        user.setCreated(new Date());
        user.setUpdated(new Date());
        if (userMapper.insert(user) != 1) {
            throw new StoreException("添加用户失败");
        }
        return 1;
    }

    private boolean getUserByName(String username) {
        EntityWrapper<User> entityWrapper = new EntityWrapper<>();
        Wrapper<User> wrapper = entityWrapper.eq("username", username);
        List<User> list = userMapper.selectList(wrapper);
        if (list.size() != 0) {
            return false;
        }
        return true;
    }

    private boolean getUserByPhone(String phone) {
        EntityWrapper<User> entityWrapper = new EntityWrapper<>();
        Wrapper<User> wrapper = entityWrapper.eq("phone", phone);
        List<User> list = userMapper.selectList(wrapper);
        if (list.size() != 0) {
            return false;
        }
        return true;
    }

    private boolean getUserByEmail(String email) {
        EntityWrapper<User> entityWrapper = new EntityWrapper<>();
        Wrapper<User> wrapper = entityWrapper.eq("email", email);
        List<User> list = userMapper.selectList(wrapper);
        if (list.size() != 0) {
            return false;
        }
        return true;
    }

    @Override
    public int updateUser(User user) {
        User old = userMapper.selectById(user.getId());
        user.setPassword(old.getPassword());
        user.setCreated(old.getCreated());
        user.setUpdated(new Date());
        if (userMapper.updateById(user) != 1) {
            throw new StoreException("更新用户失败");
        }
        return 1;
    }

    @Override
    public int changeUserState(UserVo userVo) {
        User user = userMapper.selectById(userVo.getId());
        user.setState(userVo.isState());
        user.setUpdated(new Date());
        if (userMapper.updateById(user) != 1) {
            throw new StoreException("更新用户状态失败");
        }
        return 1;
    }

    @Override
    public int changePassword(UserVo userVo) {
        User old = userMapper.selectById(userVo.getId());
        String oldMd5Pass = DigestUtils.md5DigestAsHex(userVo.getOldPwd().getBytes());
        if (!oldMd5Pass.equals(old.getPassword())){
            throw new StoreException("原密码错误");
        }
        String md5Pass = DigestUtils.md5DigestAsHex(userVo.getPassword().getBytes());
        old.setPassword(md5Pass);
        old.setUpdated(new Date());
        if (userMapper.updateById(old) != 1) {
            throw new StoreException("修改用户密码失败");
        }
        return 1;
    }

    @Override
    public int deleteUser(Long userId) {
        if (userMapper.deleteById(userId) != 1) {
            throw new StoreException("删除用户失败");
        }
        return 1;
    }

    @Override
    public ResuhSet getRoleList(int page, int limit, String condition) {
        ResuhSet result = new ResuhSet();
        PageHelper.startPage(page, limit);
        List<RoleVo> list = new ArrayList<>();
        EntityWrapper<Role> entityWrapper = new EntityWrapper<>();
        Wrapper<Role> wrapper = entityWrapper.like("name", condition);
        List<Role> roleList = roleMapper.selectList(wrapper);
        PageInfo<Role> pageInfo = new PageInfo<>(roleList);
        if (roleList.size() == 0) {
            throw new StoreException("暂无数据");
        }
        for (Role role : roleList) {
            String names = "";
            RoleVo roleVo = new RoleVo();
            roleVo.setId(role.getId());
            roleVo.setName(role.getName());
            roleVo.setDescription(role.getDescription());

            int[] permIds = permIdsByRoleId(role.getId());
            if (permIds != null){
                if (permIds.length > 1) {
                    names += permissionMapper.selectById(permIds[0]).getName();
                    for (int i = 1; permIds.length > i; i++) {
                        names += "|" + permissionMapper.selectById(permIds[i]).getName();
                    }
                } else if (permIds.length == 1) {
                    names += permissionMapper.selectById(permIds[0]).getName();
                }
            }
            roleVo.setRoles(permIdsByRoleId(role.getId()));
            roleVo.setPermissions(names);
            list.add(roleVo);
        }
        result.setRecordsTotal((int) pageInfo.getTotal());
        result.setSuccess(true);
        result.setData(list);
        return result;
    }

    @Override
    public List<Role> getAllRoles() {
        EntityWrapper<Role> entityWrapper = new EntityWrapper<>();
        return roleMapper.selectList(entityWrapper);
    }

    @Override
    public Role getRoleById(int id) {
        return roleMapper.selectById(id);
    }

    @Override
    public int addRole(Role role) {
        EntityWrapper<Role> entityWrapper = new EntityWrapper<>();
        Wrapper<Role> wrapper = entityWrapper.eq("name", role.getName());
        if (roleMapper.selectList(wrapper).size() != 0) {
            throw new StoreException("角色名已存在");
        }
        if (roleMapper.insert(role) != 1) {
            throw new StoreException("角色添加失败");
        }
        if (role.getRoleIds() != null) {
            List<Role> newRole = roleMapper.selectList(wrapper);
            for (int i = 0; i < role.getRoleIds().length; i++) {
                RolePerm rolePerm = new RolePerm();
                rolePerm.setRoleId(newRole.get(0).getId());
                rolePerm.setPermissionId(role.getRoleIds()[i]);
                if (rolePermMapper.insert(rolePerm) != 1) {
                    throw new StoreException("角色权限关系添加失败");
                }
            }
        }
        return 1;
    }

    @Override
    public int updateRole(Role role) {
        if (roleMapper.selectById(role.getId()) == null) {
            throw new StoreException("该角色不存在");
        }
        if (role.getRoleIds() != null) {
            //删除已有角色-权限
            EntityWrapper<RolePerm> entityWrapper = new EntityWrapper<>();
            Wrapper<RolePerm> wrapper = entityWrapper.eq("role_id", role.getId());
            rolePermMapper.delete(wrapper);
            //新增
            for (int i = 0; i < role.getRoleIds().length; i++) {
                RolePerm rolePerm = new RolePerm();
                rolePerm.setRoleId(role.getId());
                rolePerm.setPermissionId(role.getRoleIds()[i]);
                if (rolePermMapper.insert(rolePerm) != 1) {
                    throw new StoreException("角色权限关系表更新失败");
                }
            }
        } else {
            EntityWrapper<RolePerm> entityWrapper = new EntityWrapper<>();
            Wrapper<RolePerm> wrapper = entityWrapper.eq("role_id", role.getId());
            rolePermMapper.delete(wrapper);
        }
        if (roleMapper.updateById(role) != 1) {
            throw new StoreException("更新角色失败");
        }
        return 1;
    }

    @Override
    public int deleteRole(int id) {
        if (roleMapper.getUserRoles(id).size() != 0) {
            throw new StoreException("该角色在使用中不能删除");
        }
        EntityWrapper<RolePerm> entityWrapper = new EntityWrapper<>();
        Wrapper<RolePerm> wrapper = entityWrapper.eq("role_id", id);
        if (rolePermMapper.selectList(wrapper).size() != 0) {
            rolePermMapper.delete(wrapper);
        }
        if (roleMapper.deleteById(id) != 1) {
            throw new StoreException("删除角色失败");
        }
        return 1;
    }

    @Override
    public ResuhSet getPermissionList() {
        ResuhSet result = new ResuhSet();
        EntityWrapper<Permission> entityWrapper = new EntityWrapper<>();
        Wrapper<Permission> wrapper = entityWrapper.orderBy("order_num");
        List<Permission> permList = permissionMapper.selectList(wrapper);
        List<Permission> list = new ArrayList<>();
        if (permList.size() != 0) {
            for (Permission permission : permList) {
                permission.setChildren(new ArrayList<>());
                list.add(permission);
            }
        }
        result.setSuccess(true);
        result.setData(Permission.buildList(list, 0L));
        return result;
    }

    @Cacheable(value = "permission", key = "#userId")
    @Override
    public List<Router> routersByUserId(Long userId) {
        List<Permission> permList = permissionMapper.listPermissionUserId(userId);
        List<Router> routerList = new ArrayList<>();
        for (Permission perm : permList) {
            Router router = new Router();
            router.setId(perm.getMenuId());
            router.setParentId(perm.getParentId());
            router.setPath(perm.getUrl());
            router.setName(perm.getName());
            router.setIconCls(perm.getIcon());
            router.setMenuShow(true);
            router.setChildren(new ArrayList<>());
            router.setLeaf(perm.isLeaf());
            router.setComponent(perm.getComponent());
            routerList.add(router);
        }
        return Router.buildList(routerList, 0L);
    }

    @Override
    public int[] permIdsByRoleId(int roleId) {
        if (rolePermMapper.getPermsIdByRoleId(roleId) != null) {
            return ToolUtil.String2int(rolePermMapper.getPermsIdByRoleId(roleId));
        } else {
            return null;
        }
    }

    @Override
    public List<String> permsByUserId(Long userId) {
        List<String> permsList = new ArrayList<>();
        List<Permission> permissions = permissionMapper.listPermissionUserId(userId);
        for (Permission perm : permissions) {
            if (perm.getPerms() != null && "" != perm.getPerms()) {
                permsList.add(perm.getPerms());
            }
        }
        return permsList;
    }

    @Override
    public int addPermission(Permission permission) {
        if (permissionMapper.insert(permission) != 1) {
            throw new StoreException("添加权限失败");
        }
        return 1;
    }

    @Override
    public int updatePermission(Permission permission) {
        if (permissionMapper.updateById(permission) != 1) {
            throw new StoreException("更新权限失败");
        }
        return 1;
    }

    @Override
    public int deletePermission(Long id) {
        EntityWrapper<RolePerm> entityWrapper = new EntityWrapper<>();
        Wrapper<RolePerm> wrapper = entityWrapper.eq("permission_id", id);
        if (rolePermMapper.selectList(wrapper).size() != 0) {
            rolePermMapper.delete(wrapper);
        }
        if (permissionMapper.deleteById(id) != 1) {
            throw new StoreException("删除权限失败");
        }
        return 1;
    }

    @Override
    @CacheEvict(value = "permission", key = "#userId")
    public boolean clearCache(Long userId) {
        return true;
    }

}
