package com.xkd.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.kafka.clients.consumer.StickyAssignor;

import java.io.Serializable;

/**
 * Created by dell on 2017/11/28.
 */
@ApiModel
public class QingJiaBean implements Serializable {

    String starter;

    String asignee;

    String reason;

    Integer days;

    String startTime;

    String endTime;


    String departmentLeaderComment;

    Integer departmentLeaderPass=0;
    Integer superviserPass=0;

    String superviserComment;

    String clerkComment;


    @ApiModelProperty(value = "下一步由谁处理", required = true, example = "jim")
    public String getAsignee() {
        return asignee;
    }

    public void setAsignee(String asignee) {
        this.asignee = asignee;
    }

    @ApiModelProperty(value = "流程发起人", required = true, example = "abc")
    public String getStarter() {
        return starter;
    }

    public void setStarter(String starter) {
        this.starter = starter;
    }

    @ApiModelProperty(value = "请假原因", required = true, example = "回家结婚")
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @ApiModelProperty(value = "请假天数", required = true, example = "7")
    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    @ApiModelProperty(value = "开始时间", required = true, example = "2017-10-10 08:00:00")
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @ApiModelProperty(value = "结束时间", required = true, example = "2017-10-17 18:00:00")
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @ApiModelProperty(value = "部门负责人意见", required = true, example = "做好工作交接")
    public String getDepartmentLeaderComment() {
        return departmentLeaderComment;
    }

    public void setDepartmentLeaderComment(String departmentLeaderComment) {
        this.departmentLeaderComment = departmentLeaderComment;
    }
    @ApiModelProperty(value = "副总意见", required = true, example = "快去快回")
    public String getSuperviserComment() {
        return superviserComment;
    }

    public void setSuperviserComment(String superviserComment) {
        this.superviserComment = superviserComment;
    }

    @ApiModelProperty(value = "考勤人员核实备注", required = true, example = "考勤正常，已按时到岗")
    public String getClerkComment() {
        return clerkComment;
    }

    public void setClerkComment(String clerkComment) {
        this.clerkComment = clerkComment;
    }

    public Integer getDepartmentLeaderPass() {
        return departmentLeaderPass;
    }

    public void setDepartmentLeaderPass(Integer departmentLeaderPass) {
        this.departmentLeaderPass = departmentLeaderPass;
    }
    @ApiModelProperty(value = "副总是否通过", required = true, example = "1")
    public Integer getSuperviserPass() {
        return superviserPass;
    }
    @ApiModelProperty(value = "部门领导是否通过", required = true, example = "1")
    public void setSuperviserPass(Integer superviserPass) {
        this.superviserPass = superviserPass;
    }
}
