package com.kuangchi.sdd.businessConsole.process.model;

public class ForgetCheckModel {
	private Integer id;
	private String staffNum;
	private String reason;
	private String time;
	private String forgetPoint;
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
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getForgetPoint() {
		return forgetPoint;
	}
	public void setForgetPoint(String forgetPoint) {
		this.forgetPoint = forgetPoint;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
	
}
