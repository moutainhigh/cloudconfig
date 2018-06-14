package com.kuangchi.sdd.attendanceConsole.statistic.model;

public class AttendanceCheck {
	  Integer id;
	  String staffNum;
	  String staffName;
	  String checktime;
	  String flagStatus; //'是否补打卡  0  否  1  是'
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
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public String getChecktime() {
		return checktime;
	}
	public void setChecktime(String checktime) {
		this.checktime = checktime;
	}
	public String getFlagStatus() {
		return flagStatus;
	}
	public void setFlagStatus(String flagStatus) {
		this.flagStatus = flagStatus;
	}
	
	  
	  
	  
	  
}
