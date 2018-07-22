package com.wjh.menuoperateservicemodel.model;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class OperateAddDto {
     private Long menuId;
    private String url;
    private String operateName;
    private String operateCode;
    private Integer orderNo;



    @ApiModelProperty(value = "菜单Id", required = true, example = "123456")
    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    @ApiModelProperty(value = "权限URL", required = true, example = "/user/delete")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @ApiModelProperty(value = "权限名称", required = true, example = "删除用户")
    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    @ApiModelProperty(value = "权限代码", required = true, example = "U0001")
    public String getOperateCode() {
        return operateCode;
    }

    public void setOperateCode(String operateCode) {
        this.operateCode = operateCode;
    }

    @ApiModelProperty(value = "排序号", required = true, example = "1")
    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
