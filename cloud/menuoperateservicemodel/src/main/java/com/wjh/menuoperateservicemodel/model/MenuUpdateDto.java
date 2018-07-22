package com.wjh.menuoperateservicemodel.model;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class MenuUpdateDto {

    private Long id;
    private String parentMenuId;
    private String menuName;
    private Integer innerOrder;
    private String remark;
    private String routerName;
    private String routerPath;
    private String iconNormalPath;
    private String iconCheckPath;

    @ApiModelProperty(value = "id", required = true, example = "123456")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ApiModelProperty(value = "父菜单Id", required = true, example = "123456")
    public String getParentMenuId() {
        return parentMenuId;
    }

    public void setParentMenuId(String parentMenuId) {
        this.parentMenuId = parentMenuId;
    }


    @ApiModelProperty(value = "菜单名称", required = true, example = "用户管理")
    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    @ApiModelProperty(value = "组内排序", required = true, example = "0")
    public Integer getInnerOrder() {
        return innerOrder;
    }

    public void setInnerOrder(Integer innerOrder) {
        this.innerOrder = innerOrder;
    }

    @ApiModelProperty(value = "备注", required = true, example = "这是一个用户管理菜单")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @ApiModelProperty(value = "路由名称", required = true, example = "用户管理")
    public String getRouterName() {
        return routerName;
    }

    public void setRouterName(String routerName) {
        this.routerName = routerName;
    }

    @ApiModelProperty(value = "路由", required = true, example = "/user")
    public String getRouterPath() {
        return routerPath;
    }

    public void setRouterPath(String routerPath) {
        this.routerPath = routerPath;
    }


    @ApiModelProperty(value = "未选中图标路径", required = true, example = "http://localhost:9090/icons/userIcon.png")
    public String getIconNormalPath() {
        return iconNormalPath;
    }

    public void setIconNormalPath(String iconNormalPath) {
        this.iconNormalPath = iconNormalPath;
    }

    @ApiModelProperty(value = "选中后图标路径", required = true, example = "http://localhost:9090/icons/userIcon2.png")
    public String getIconCheckPath() {
        return iconCheckPath;
    }

    public void setIconCheckPath(String iconCheckPath) {
        this.iconCheckPath = iconCheckPath;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
