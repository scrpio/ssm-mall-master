package com.mall.manager.shiro;

import com.mall.common.util.ToolUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;

public class ShiroKit {
    /**
     * 加盐参数
     */
    public final static String hashAlgorithmName = "MD5";

    /**
     * 循环次数
     */
    public final static int hashIterations = 1024;

    /**
     * shiro密码加密工具类
     *
     * @param credentials 密码
     * @param saltSource 密码盐
     * @return
     */
    public static String md5(String credentials, String saltSource) {
        ByteSource salt = new Md5Hash(saltSource);
        return new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations).toString();
    }

    /**
     * 获取随机盐值
     * @param length
     * @return
     */
    public static String getRandomSalt(int length) {
        return ToolUtil.getRandomString(length);
    }

    /**
     * 获取当前Subject
     * @return
     */
    public static Subject getSubject(){
        return SecurityUtils.getSubject();
    }

    /**
     * 认证通过或已记住客户。与guset搭配使用。
     * @return 客户：true，否则 false
     */
    public static boolean isUser(){
        return getSubject() != null && getSubject().getPrincipal() != null;
    }

    /**
     * 验证当前用户是否为“访客”，即未认证（包含未记住）的用户。用user搭配使用
     * @return 访客：true，否则false
     */
    public static boolean isGust(){
        return !isUser();
    }
    public static ShiroUser getUser(){
        if (isGust()){
            return null;
        }else {
            return (ShiroUser)getSubject().getPrincipals().getPrimaryPrincipal();
        }
    }
}
