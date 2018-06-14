package com.kuangchi.sdd.businessConsole.test.model;

import com.kuangchi.sdd.base.model.BaseModelSupport;

/**
 * Created by jianhui.wu on 2016/2/2.
 */
public class TestBean  extends BaseModelSupport {
    private  String id;
    private  String name;
    private  Integer status;
    private String remark;
    private String loginDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
    }
}
