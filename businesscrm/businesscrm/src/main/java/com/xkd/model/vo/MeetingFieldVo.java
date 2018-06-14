package com.xkd.model.vo;

import com.xkd.model.groups.SaveValidateGroup;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by dell on 2018/4/20.
 */
public class MeetingFieldVo {
    private String id;
    private String meetingId;
    @NotNull(groups = {SaveValidateGroup.class})
    private String field;
    @NotNull(groups = {SaveValidateGroup.class})
    private String fieldName;
    @NotNull(groups = {SaveValidateGroup.class})
    private Integer isRequred;


    @ApiModelProperty(value = "id",required = true,example = "123456")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ApiModelProperty(value = "会务Id",required = true,example = "123456")
    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    @ApiModelProperty(value = "字段key",required = true,example = "name")
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @ApiModelProperty(value = "字段名称",required = true,example = "姓名")
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @ApiModelProperty(value = "是否必须 0 否  1 是",required = true,example = "0")
    public Integer getIsRequred() {
        return isRequred;
    }

    public void setIsRequred(Integer isRequred) {
        this.isRequred = isRequred;
    }
}
