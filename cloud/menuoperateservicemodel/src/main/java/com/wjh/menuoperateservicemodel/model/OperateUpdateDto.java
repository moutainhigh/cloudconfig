package com.wjh.menuoperateservicemodel.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class OperateUpdateDto {
    private Long id;
    private Long menuId;
    private String serviceName;
    private String url;
    private String operateName;
    private String operateCode;
    private Integer orderNo;


    @ApiModelProperty(value = "Id", required = true, example = "123456")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ApiModelProperty(value = "菜单Id", required = true, example = "123456")
    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    @ApiModelProperty(value = "微服务名称,全部为小写：", required = true, example = "userservice")
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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


}
