package com.xkd.model;

import java.util.Date;

public class SpreadSetting {

    private String id;
    private String inviteTitle;
    private String  productionTypeId;
    private String  productionId;
    private String  productionName;
    private String  getRate;
    private String  createdBy;
    private String  createDateStr;
    private Date  createDate;
    private String  updatedBy;
    private String  updateDateStr;
    private Date updateDate;
    private Integer vflag;
    private Integer status;
    private Integer productionTableType;
    private Object object;

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

    public String getProductionName() {
        return productionName;
    }

    public void setProductionName(String productionName) {
        this.productionName = productionName;
    }

    public Integer getProductionTableType() {
        return productionTableType;
    }

    public void setProductionTableType(Integer productionTableType) {
        this.productionTableType = productionTableType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getInviteTitle() {
        return inviteTitle;
    }

    public void setInviteTitle(String inviteTitle) {
        this.inviteTitle = inviteTitle;
    }

    public String getProductionTypeId() {
        return productionTypeId;
    }

    public void setProductionTypeId(String productionTypeId) {
        this.productionTypeId = productionTypeId;
    }

    public String getGetRate() {
        return getRate;
    }

    public void setGetRate(String getRate) {
        this.getRate = getRate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getVflag() {
        return vflag;
    }

    public void setVflag(Integer vflag) {
        this.vflag = vflag;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getProductionId() {
        return productionId;
    }

    public void setProductionId(String productionId) {
        this.productionId = productionId;
    }
}
