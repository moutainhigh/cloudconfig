package com.xkd.model;

/**
 * Created by dell on 2018/4/27.
 */
public class MeetingFieldPo extends BasePo {
    private String id;
    private String meetingId;
    private String field;
    private String fieldName;
    private Integer isRequred;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Integer getIsRequred() {
        return isRequred;
    }

    public void setIsRequred(Integer isRequred) {
        this.isRequred = isRequred;
    }


}
