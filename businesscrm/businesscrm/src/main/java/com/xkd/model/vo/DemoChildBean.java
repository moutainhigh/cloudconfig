package com.xkd.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by dell on 2017/11/24.
 */
@ApiModel
public class DemoChildBean {
    String school;
    String teacher;
    @ApiModelProperty(value = "学校",required = true,example = "中心小学")
    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }


    @ApiModelProperty(value = "老师",required = true,example = "王老师")
    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
