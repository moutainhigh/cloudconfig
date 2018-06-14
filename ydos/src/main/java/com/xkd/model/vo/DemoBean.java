package com.xkd.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by dell on 2017/11/24.
 */
@ApiModel
public class DemoBean {
    String name;

    Integer age;

    Double score;


    String birthday;

    DemoChildBean demoChildBean;

    @ApiModelProperty(value = "姓名",required = true, example = "三二麻子")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ApiModelProperty(value = "年龄", required = true, example = "15")
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @ApiModelProperty(value = "生日", required = true, example = "2012-10-11 12:10:10")
    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @ApiModelProperty(value = "老师" ,required = true)
    public DemoChildBean getDemoChildBean() {
        return demoChildBean;
    }

    public void setDemoChildBean(DemoChildBean demoChildBean) {
        this.demoChildBean = demoChildBean;
    }

    @ApiModelProperty(value = "分数", required = true, example = "85.2")
    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
