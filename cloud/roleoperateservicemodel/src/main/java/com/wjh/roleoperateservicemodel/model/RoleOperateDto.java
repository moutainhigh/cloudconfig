package com.wjh.roleoperateservicemodel.model;

import io.swagger.annotations.ApiModel;

import java.util.Date;
import java.util.List;


@ApiModel
public class RoleOperateDto {

    private Long roleId;
    private List<Long> operateIdList;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public List<Long> getOperateIdList() {
        return operateIdList;
    }

    public void setOperateIdList(List<Long> operateIdList) {
        this.operateIdList = operateIdList;
    }
}
