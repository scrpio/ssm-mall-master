package com.mall.manager.model.Vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ZTreeNode implements Serializable {

    private int id;

    private String value;

    private int pId;

    private String title;

    private Boolean isParent;

    private Boolean open;

    private String icon;

    private boolean status;

    private int sortOrder;

    private String remark;

    /**
     * 板块限制商品数量
     */
    private int limitNum;

    /**
     * 板块类型
     */
    private int type;
    private List<ZTreeNode> children;

    public List<ZTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<ZTreeNode> children) {
        this.children = children;
    }

    public static List<ZTreeNode> buildList(List<ZTreeNode> nodes, Integer idParam) {
        if (nodes == null) {
            return null;
        }
        List<ZTreeNode> topNodes = new ArrayList<ZTreeNode>();
        for (ZTreeNode child : nodes) {
            Integer pid = child.getpId();
            if (pid == null || idParam == pid) {
                topNodes.add(child);
                continue;
            }
            for (ZTreeNode parent : nodes) {
                Integer id = parent.getId();
                if (id != null && id.equals(pid)) {
                    parent.getChildren().add(child);
                    continue;
                }
            }
        }
        return topNodes;
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(Boolean isParent) {
        this.isParent = isParent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }
}
