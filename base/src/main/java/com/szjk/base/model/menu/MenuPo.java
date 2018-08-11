package com.szjk.base.model.menu;

import java.util.Date;

public class MenuPo {

    private Long id;
    private String parentMenuId;
    private String menuName;
    private Integer innerOrder;
    private String remark;
    private String routerName;
    private String routerPath;
    private String iconNormalPath;
    private String iconCheckPath;
    private int isCurrent;
    private Long createdBy;
    private Date createDate;
    private Long updatedBy;
    private Date updateDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParentMenuId() {
        return parentMenuId;
    }

    public void setParentMenuId(String parentMenuId) {
        this.parentMenuId = parentMenuId;
    }


    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Integer getInnerOrder() {
        return innerOrder;
    }

    public void setInnerOrder(Integer innerOrder) {
        this.innerOrder = innerOrder;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRouterName() {
        return routerName;
    }

    public void setRouterName(String routerName) {
        this.routerName = routerName;
    }

    public String getRouterPath() {
        return routerPath;
    }

    public void setRouterPath(String routerPath) {
        this.routerPath = routerPath;
    }


    public String getIconNormalPath() {
        return iconNormalPath;
    }

    public void setIconNormalPath(String iconNormalPath) {
        this.iconNormalPath = iconNormalPath;
    }

    public String getIconCheckPath() {
        return iconCheckPath;
    }

    public void setIconCheckPath(String iconCheckPath) {
        this.iconCheckPath = iconCheckPath;
    }


    public int getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(int isCurrent) {
        this.isCurrent = isCurrent;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
