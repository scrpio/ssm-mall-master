package com.mall.manager.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@TableName("tb_permission")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "menu_id", type = IdType.AUTO)
    private Long menuId;
    //父菜单ID，一级菜单为0
    @TableField(value = "parent_id")
    private Long parentId;

    private String name;

    /**
     * url地址
     */
    private String url;
    /**
     * 授权
     */
    private String perms;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 菜单排序号
     */
    @TableField(value = "order_num")
    private Integer orderNum;
    /**
     * 类型   0：目录   1：菜单   2：按钮
     */
    private Integer type;

    private boolean leaf;

    private String component;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created;
    @TableField(exist = false)
    private List<Permission> children;

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<Permission> getChildren() {
        return children;
    }

    public void setChildren(List<Permission> children) {
        this.children = children;
    }

    public static List<Permission> buildList(List<Permission> nodes, Long idParam) {
        if (nodes == null) {
            return null;
        }
        List<Permission> topNodes = new ArrayList<Permission>();
        for (Permission child : nodes) {
            Long pid = child.getParentId();
            if (pid == null || idParam == pid) {
                topNodes.add(child);
                continue;
            }
            for (Permission parent : nodes) {
                Long id = parent.getMenuId();
                if (id != null && id.equals(pid)) {
                    parent.getChildren().add(child);
                    continue;
                }
            }
        }
        return topNodes;
    }
}
