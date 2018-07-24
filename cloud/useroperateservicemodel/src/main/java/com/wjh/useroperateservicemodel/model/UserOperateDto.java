package com.wjh.useroperateservicemodel.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApiModel
public class UserOperateDto {

    private Long userId;
    private List<Long> operateIdList;


    @ApiModelProperty(value = "用户ID", required = true, example = "123456")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @ApiModelProperty(value = "权限Id列表", required = true)
    public List<Long> getOperateIdList() {
        return operateIdList;
    }

    public void setOperateIdList(List<Long> operateIdList) {
        this.operateIdList = operateIdList;
    }
}
