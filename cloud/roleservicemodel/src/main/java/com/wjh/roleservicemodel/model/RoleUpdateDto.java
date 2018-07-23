package com.wjh.roleservicemodel.model;

import io.swagger.annotations.ApiModelProperty;

public class RoleUpdateDto {
    private Long id;
    private String roleName;
    private String remark;

    @ApiModelProperty(value = "id",required = true,example = "123456")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ApiModelProperty(value = "角色名称",required = true,example = "管理员")
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    @ApiModelProperty(value = "备注",required = true,example = "管理员负责管理公司所有角色。")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
