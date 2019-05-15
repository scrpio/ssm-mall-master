package com.mall.manager.controller;

import com.mall.manager.model.Vo.ResuhSet;
import com.mall.manager.service.IUserService;
import com.mall.manager.shiro.ShiroKit;
import com.mall.common.result.Result;
import com.mall.common.result.ResultUtil;
import com.mall.manager.model.Permission;
import com.mall.manager.model.Role;
import com.mall.manager.model.User;
import com.mall.manager.model.Vo.Router;
import com.mall.manager.model.Vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(description = "管理员管理")
public class UserController {
    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/user/info", method = RequestMethod.GET)
    @ApiOperation(value = "获取登录用户信息")
    public Result<User> getUserInfo(@RequestParam String token) {
        User user = userService.getUserByUsername(token);
        user.setPassword(null);
        return new ResultUtil<User>().setData(user);
    }

    @RequestMapping(value = "/user/userList", method = RequestMethod.GET)
    @ApiOperation(value = "获取用户列表")
    public ResuhSet getUserList(@RequestParam int page, @RequestParam int limit, @RequestParam String condition) {
        ResuhSet result = userService.getUserList(page, limit, condition);
        return result;
    }

    @RequestMapping(value = "/user/addUser", method = RequestMethod.POST)
    @ApiOperation(value = "添加用户")
    public Result<Object> addUser(@RequestBody User user) {
        userService.addUser(user);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/user/updateUser", method = RequestMethod.POST)
    @ApiOperation(value = "更新用户")
    public Result<Object> updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/user/state", method = RequestMethod.POST)
    @ApiOperation(value = "更新用户状态")
    public Result<Object> updateUserStatus(@RequestBody UserVo userVo) {
        userService.changeUserState(userVo);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/user/changePass", method = RequestMethod.POST)
    @ApiOperation(value = "修改用户密码")
    public Result<Object> changePass(@RequestBody UserVo userVo) {
        userService.changePassword(userVo);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/user/delUser/{ids}", method = RequestMethod.POST)
    @ApiOperation(value = "删除用户")
    public Result<Object> delUser(@PathVariable Long[] ids) {
        for (Long id : ids) {
            userService.deleteUser(id);
        }
        return new ResultUtil<Object>().setData(null);
    }
    @RequestMapping(value = "/user/roleList", method = RequestMethod.GET)
    @ApiOperation(value = "获取角色列表")
    public ResuhSet getRoleList(@RequestParam int page, @RequestParam int limit, @RequestParam String condition) {
        ResuhSet result = userService.getRoleList(page, limit, condition);
        return result;
    }

    @RequestMapping(value = "/user/getAllRoles", method = RequestMethod.GET)
    @ApiOperation(value = "获取所有角色")
    public Result<List<Role>> getAllRoles() {
        List<Role> list = userService.getAllRoles();
        return new ResultUtil<List<Role>>().setData(list);
    }

    @RequestMapping(value = "/user/getRole/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "根据ID获取角色")
    public Role getRole(@PathVariable int id) {
        return userService.getRoleById(id);
    }

    @RequestMapping(value = "/user/addRole", method = RequestMethod.POST)
    @ApiOperation(value = "添加角色")
    public Result<Object> addRole(@RequestBody Role role) {
        userService.addRole(role);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/user/updateRole", method = RequestMethod.POST)
    @ApiOperation(value = "更新角色")
    public Result<Object> updateRole(@RequestBody Role role) {
        userService.updateRole(role);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/user/delRole/{ids}", method = RequestMethod.POST)
    @ApiOperation(value = "删除角色")
    public Result<Object> delRole(@PathVariable int[] ids) {
        for (int id : ids) {
            int result = userService.deleteRole(id);
            if (result == 0) {
                return new ResultUtil<Object>().setErrorMsg("id为" + id + "的角色被使用中，不能删除！");
            }
        }
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/user/permissionList", method = RequestMethod.GET)
    @ApiOperation(value = "获取权限列表")
    public ResuhSet getPermissionList() {
        ResuhSet result = userService.getPermissionList();
        return result;
    }

    @RequestMapping(value = "/user/treePerms", method = RequestMethod.GET)
    @ApiOperation(value = "获取树状菜单")
    public List<Router> getMenus() {
        List<Router> perms = userService.routersByUserId(ShiroKit.getUser().getId());
        return perms;
    }

    @RequestMapping(value = "/user/permIds/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "根据角色roleId获取权限permIds")
    public Result<Object> getPermsIds(@PathVariable int id) {
        int[] arr = userService.permIdsByRoleId(id);
        return new ResultUtil<Object>().setData(arr);
    }

    @RequestMapping(value = "/user/addPermission", method = RequestMethod.POST)
    @ApiOperation(value = "添加权限")
    public Result<Object> addPermission(@RequestBody Permission permission) {
        userService.addPermission(permission);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/user/updatePermission", method = RequestMethod.POST)
    @ApiOperation(value = "更新权限")
    public Result<Object> updatePermission(@RequestBody Permission permission) {
        userService.updatePermission(permission);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/user/delPermission/{ids}", method = RequestMethod.POST)
    @ApiOperation(value = "删除权限")
    public Result<Object> delPermission(@PathVariable Long[] ids) {
        for (Long id : ids) {
            userService.deletePermission(id);
        }
        return new ResultUtil<Object>().setData(null);
    }

}
