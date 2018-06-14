package com.xkd.model;


import java.util.Date;

/**
 * 营销推广的推广用户
 */
public class SpreadUser {

    private String id;
    private String uname;
    private String mobile;
    private String password;
    private String createdBy;
    private String createDateStr;
    private Date createDate;
    private String updatedBy;
    private String updateDateStr;
    private Date updateDate;
    private Integer status;
    private String accountName;
    private String accountCard;
    private String accountBank;
    private String accountSaving;
    private String openId;
    private Integer uflag;

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public String getUpdateDateStr() {
        return updateDateStr;
    }

    public void setUpdateDateStr(String updateDateStr) {
        this.updateDateStr = updateDateStr;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountCard() {
        return accountCard;
    }

    public Integer getUflag() {
        return uflag;
    }

    public void setUflag(Integer uflag) {
        this.uflag = uflag;
    }

    public void setAccountCard(String accountCard) {
        this.accountCard = accountCard;
    }

    public String getAccountBank() {
        return accountBank;
    }

    public void setAccountBank(String accountBank) {
        this.accountBank = accountBank;
    }

    public String getAccountSaving() {
        return accountSaving;
    }

    public void setAccountSaving(String accountSaving) {
        this.accountSaving = accountSaving;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getMobile() {
        return mobile;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
