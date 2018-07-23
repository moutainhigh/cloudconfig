package com.wjh.userservicemodel.model;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModelProperty;

public class UserUpdateDto {

    private Long id;
    private String name;
    private String nickname;
    private String birthday;
    private String mobile;
    private String img;
    private String gender;
    private String password;

    @ApiModelProperty(value = "id", required = true, example = "123456")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ApiModelProperty(value = "姓名", required = true, example = "小明")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ApiModelProperty(value = "昵称", required = true, example = "少女杀手")
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @ApiModelProperty(value = "出生日期", required = true, example = "1987-01-01")
    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @ApiModelProperty(value = "手机号码", required = true, example = "13000000000")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @ApiModelProperty(value = "头像URL", required = true, example = "https://www.baidu.com/img/bd_logo1.png")
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @ApiModelProperty(value = "性别  0 未知  1 女  2 男 ", required = true, example = "0")
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @ApiModelProperty(value = "密码 MD5 ", required = true, example = "e10adc3949ba59abbe56e057f20f883e")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
