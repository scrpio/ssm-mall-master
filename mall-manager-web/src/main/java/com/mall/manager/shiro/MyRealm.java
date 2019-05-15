package com.mall.manager.shiro;

import com.mall.manager.model.User;
import com.mall.manager.service.IUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Set;

public class MyRealm extends AuthorizingRealm {
    private static final Logger log = LoggerFactory.getLogger(MyRealm.class);

    @Resource
    private IUserService userService;

    /**
     * 权限认证
     * @param principal
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        log.info("开始权限认证");
        //获取用户名
        String username = principal.getPrimaryPrincipal().toString();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //获得授权角色
        authorizationInfo.setRoles(userService.getRoles(username));
        //获得授权权限
        authorizationInfo.setStringPermissions(userService.getPermissions(username));
        return authorizationInfo;
    }

    /**
     * 登录认证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("开始登录认证");
        //获取用户名密码
        String username = token.getPrincipal().toString();
        User user = userService.getUserByUsername(username);
        if (user != null) {
            Set<String> urls = userService.getPermissions(username);
            Set<String> roles = userService.getRoles(username);
            ShiroUser shiroUser = new ShiroUser(user.getId(), user.getUsername(), urls);
            shiroUser.setRoles(roles);
            //得到用户账号和密码存放到authenticationInfo中用于Controller层的权限判断 第三个参数随意不能为null
            return new SimpleAuthenticationInfo(shiroUser, user.getPassword(), super.getName());
        } else {
            return null;
        }
    }
}
