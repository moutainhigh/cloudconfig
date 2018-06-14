package com.xkd.model;

import java.util.Date;

public class YDrepaire {
    private String id;

    private String applyId;

    private String pcCompanyId;

    private String repaireNo;

    private String deviceId;

    private String dueTime;

    private String description;

    private String dealDescription;

    private String summary;

    private Integer statusFlag;

    private Integer responseScore;

    private Integer altitudeScore;

    private Integer professionScore;

    private String customerFeedback;

    private Date createDate;

    private String createdBy;

    private Date updateDate;

    private String updatedBy;

    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId == null ? null : applyId.trim();
    }

    public String getPcCompanyId() {
        return pcCompanyId;
    }

    public void setPcCompanyId(String pcCompanyId) {
        this.pcCompanyId = pcCompanyId == null ? null : pcCompanyId.trim();
    }

    public String getRepaireNo() {
        return repaireNo;
    }

    public void setRepaireNo(String repaireNo) {
        this.repaireNo = repaireNo == null ? null : repaireNo.trim();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime == null ? null : dueTime.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getDealDescription() {
        return dealDescription;
    }

    public void setDealDescription(String dealDescription) {
        this.dealDescription = dealDescription == null ? null : dealDescription.trim();
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary == null ? null : summary.trim();
    }

    public Integer getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(Integer statusFlag) {
        this.statusFlag = statusFlag;
    }

    public Integer getResponseScore() {
        return responseScore;
    }

    public void setResponseScore(Integer responseScore) {
        this.responseScore = responseScore;
    }

    public Integer getAltitudeScore() {
        return altitudeScore;
    }

    public void setAltitudeScore(Integer altitudeScore) {
        this.altitudeScore = altitudeScore;
    }

    public Integer getProfessionScore() {
        return professionScore;
    }

    public void setProfessionScore(Integer professionScore) {
        this.professionScore = professionScore;
    }

    public String getCustomerFeedback() {
        return customerFeedback;
    }

    public void setCustomerFeedback(String customerFeedback) {
        this.customerFeedback = customerFeedback == null ? null : customerFeedback.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy == null ? null : updatedBy.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}