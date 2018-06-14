package com.kuangchi.sdd.businessConsole.process.model;

public class LeaveModel {

	  private Integer id;
	  private String staffNum;
	  private String reason;
	  private String fromTime;
	  private String toTime;
	  private String leaveCategory;
	  private Integer totalTime;
	  private String processInstanceId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStaffNum() {
		return staffNum;
	}
	public void setStaffNum(String staffNum) {
		this.staffNum = staffNum;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getFromTime() {
		return fromTime;
	}
	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}
	public String getToTime() {
		return toTime;
	}
	public void setToTime(String toTime) {
		this.toTime = toTime;
	}
	public String getLeaveCategory() {
		return leaveCategory;
	}
	public void setLeaveCategory(String leaveCategory) {
		this.leaveCategory = leaveCategory;
	}
	public Integer getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(Integer totalTime) {
		this.totalTime = totalTime;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	  
	  
	  
}
