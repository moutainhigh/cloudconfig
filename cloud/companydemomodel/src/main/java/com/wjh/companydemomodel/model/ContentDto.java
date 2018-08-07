package com.wjh.companydemomodel.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ContentDto {
    private String contactor;
    private int score;

    @ApiModelProperty(value = "联系人",required = true,example = "jim")
    public String getContactor() {
        return contactor;
    }

    public void setContactor(String contactor) {
        this.contactor = contactor;
    }

    @ApiModelProperty(value = "完整度",required = true,example = "20")
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
