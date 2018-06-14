package com.xkd.model;

/**
 * Create by @author: wanghuijiu; @date: 18-2-28;
 */
public class DcYdReportSheet {

    private String id;
    private String reportId;
    private String obejectId;
    private Integer flag;
    private Integer completeFlag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getObejectId() {
        return obejectId;
    }

    public void setObejectId(String obejectId) {
        this.obejectId = obejectId;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getCompleteFlag() {
        return completeFlag;
    }

    public void setCompleteFlag(Integer completeFlag) {
        this.completeFlag = completeFlag;
    }
}
