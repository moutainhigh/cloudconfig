package com.wjh.companydemomodel.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ContactorDto {
    private String name;
    private String mobile;

    @ApiModelProperty(value = "姓名",required = true,example = "小明")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ApiModelProperty(value = "手机",required = true,example = "13000000000")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
