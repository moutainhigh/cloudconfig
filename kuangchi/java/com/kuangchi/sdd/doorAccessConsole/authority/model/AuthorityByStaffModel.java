package com.kuangchi.sdd.doorAccessConsole.authority.model;

public class AuthorityByStaffModel {
	private String staffNum;
	private String staffNo;
	private String staffName;
	private String doorNum;
	private String doorName;
	private String deviceNum;
	 private String validStartTime;
	 private String validEndTime;
	 private String timeGroupNum;
	 private String timeGroupName;
	 private String taskState;
	 
	 
	public String getTaskState() {
		return taskState;
	}
	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}
	public String getStaffNo() {
		return staffNo;
	}
	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}
	public String getStaffNum() {
		return staffNum;
	}
	public void setStaffNum(String staffNum) {
		this.staffNum = staffNum;
	}
	public String getDoorNum() {
		return doorNum;
	}
	public void setDoorNum(String doorNum) {
		this.doorNum = doorNum;
	}
	
	public String getDoorName() {
		return doorName;
	}
	public void setDoorName(String doorName) {
		this.doorName = doorName;
	}
	
	public String getDeviceNum() {
		return deviceNum;
	}
	public void setDeviceNum(String deviceNum) {
		this.deviceNum = deviceNum;
	}
	public String getValidStartTime() {
		return validStartTime;
	}
	public void setValidStartTime(String validStartTime) {
		this.validStartTime = validStartTime;
	}
	public String getValidEndTime() {
		return validEndTime;
	}
	public void setValidEndTime(String validEndTime) {
		this.validEndTime = validEndTime;
	}
	public String getTimeGroupNum() {
		return timeGroupNum;
	}
	public void setTimeGroupNum(String timeGroupNum) {
		this.timeGroupNum = timeGroupNum;
	}
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public String getTimeGroupName() {
		return timeGroupName;
	}
	public void setTimeGroupName(String timeGroupName) {
		this.timeGroupName = timeGroupName;
	}
	
}
