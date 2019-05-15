package com.mall.manager.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@TableName("tb_menu")
public class Menu implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String icon;
    private String path;
    private String authority;
    @TableField(value = "hide_in_menu")
    private boolean hideInMenu;
    private Integer sort;
    private Integer type;
    @TableField(value = "parent_id")
    private Integer parentId;
    @TableField(exist = false)
    private List<Menu> children;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public boolean isHideInMenu() {
        return hideInMenu;
    }

    public void setHideInMenu(boolean hideInMenu) {
        this.hideInMenu = hideInMenu;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    public static List<Menu> buildList(List<Menu> nodes, Integer idParam) {
        if (nodes == null) {
            return null;
        }
        List<Menu> topNodes = new ArrayList<Menu>();
        for (Menu child : nodes) {
            Integer pid = child.getParentId();
            if (pid == null || idParam == pid) {
                topNodes.add(child);
                continue;
            }
            for (Menu parent : nodes) {
                Integer id = parent.getId();
                if (id != null && id.equals(pid)) {
                    parent.getChildren().add(child);
                    continue;
                }
            }
        }
        return topNodes;
    }
}
