package com.wjh.companydemomodel.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ContentDto {
    private String representative;
    private int score;
    private String manageScope;


    @ApiModelProperty(value = "法人代表",required = true,example = "jim")
    public String getRepresentative() {
        return representative;
    }

    public void setRepresentative(String representative) {
        this.representative = representative;
    }



    @ApiModelProperty(value = "完整度",required = true,example = "20")
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @ApiModelProperty(value = "经营范围",required = true,example = "家具，家电，维修")
    public String getManageScope() {
        return manageScope;
    }

    public void setManageScope(String manageScope) {
        this.manageScope = manageScope;
    }
}
