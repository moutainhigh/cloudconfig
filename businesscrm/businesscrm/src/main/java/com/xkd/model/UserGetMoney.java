package com.xkd.model;

import java.util.Date;

public class UserGetMoney {
    private String id;
    private String userOrderId;
    private String userSpreadId;
    private String getMoney;
    private Date createDate;
    private String createDateStr;
    private Integer logFlag;
    private Integer logStauts;

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public String getId() {
        return id;
    }

    public Integer getLogFlag() {
        return logFlag;
    }

    public Integer getLogStauts() {
        return logStauts;
    }

    public void setLogStauts(Integer logStauts) {
        this.logStauts = logStauts;
    }

    public void setLogFlag(Integer logFlag) {
        this.logFlag = logFlag;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getUserSpreadId() {
        return userSpreadId;
    }

    public void setUserSpreadId(String userSpreadId) {
        this.userSpreadId = userSpreadId;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserOrderId() {
        return userOrderId;
    }

    public void setUserOrderId(String userOrderId) {
        this.userOrderId = userOrderId;
    }

    public String getGetMoney() {
        return getMoney;
    }

    public void setGetMoney(String getMoney) {
        this.getMoney = getMoney;
    }
}
