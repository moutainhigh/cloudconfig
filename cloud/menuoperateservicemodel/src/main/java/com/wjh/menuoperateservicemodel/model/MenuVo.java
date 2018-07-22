package com.wjh.menuoperateservicemodel.model;

import java.sql.Date;
import java.sql.Timestamp;

public class MenuVo {

    private Long id;
    private String parentMenuId;
    private String menuName;
    private Integer innerOrder;
    private String remark;
    private String routerName;
    private String routerPath;
    private String iconNormalPath;
    private String iconCheckPath;
    private String createdBy;
    private String createDate;
    private String updatedBy;
    private String updateDate;


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



    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
