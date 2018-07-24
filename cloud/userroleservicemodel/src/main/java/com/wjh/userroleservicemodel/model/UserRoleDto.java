package com.wjh.userroleservicemodel.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


@ApiModel
public class UserRoleDto {
    private Long userId;
    private List<Long> roleIdList;

    @ApiModelProperty(value = "用户Id", required = true, example = "123456")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    @ApiModelProperty(value = "菜单Id列表", required = true)
    public List<Long> getRoleIdList() {
        return roleIdList;
    }

    public void setRoleIdList(List<Long> roleIdList) {
        this.roleIdList = roleIdList;
    }
}
