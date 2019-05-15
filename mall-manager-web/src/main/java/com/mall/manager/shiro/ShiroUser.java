package com.mall.manager.shiro;

import java.io.Serializable;
import java.util.Set;

public class ShiroUser implements Serializable {
    private static final long serialVersionUID = -1373760761780840081L;

    private Long id;
    private final String username;
    private Set<String> urlSet;
    private Set<String> roles;

    public ShiroUser(String username) {
        this.username = username;
    }

    public ShiroUser(Long id, String username, Set<String> urlSet) {
        this.id = id;
        this.username = username;
        this.urlSet = urlSet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<String> getUrlSet() {
        return urlSet;
    }

    public void setUrlSet(Set<String> urlSet) {
        this.urlSet = urlSet;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    /**
     * 本函数输出将作为默认的<shiro:principal/>输出.
     */
    @Override
    public String toString() {
        return username;
    }
}
