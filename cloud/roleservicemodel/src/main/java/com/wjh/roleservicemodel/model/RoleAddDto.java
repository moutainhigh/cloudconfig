package com.wjh.roleservicemodel.model;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel
public class RoleAddDto {
    private String roleName;
    private String remark;

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


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
