package com.kuangchi.sdd.attendanceConsole.attendCount.model;

public class AttendViewDateInfoModel {
	private String staffNum;
	private String staffName;
	private String deptName;
	private String leaveSTime;//请假开始时间
	private String leaveETime;//请假结束时间
	private String outSTime;//外出开始时间
	private String outETime;//外出结束时间
	private String leaveCatetory;//请假类型
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
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getLeaveSTime() {
		return leaveSTime;
	}
	public void setLeaveSTime(String leaveSTime) {
		this.leaveSTime = leaveSTime;
	}
	public String getLeaveETime() {
		return leaveETime;
	}
	public void setLeaveETime(String leaveETime) {
		this.leaveETime = leaveETime;
	}
	public String getOutSTime() {
		return outSTime;
	}
	public void setOutSTime(String outSTime) {
		this.outSTime = outSTime;
	}
	public String getOutETime() {
		return outETime;
	}
	public void setOutETime(String outETime) {
		this.outETime = outETime;
	}
	public String getLeaveCatetory() {
		return leaveCatetory;
	}
	public void setLeaveCatetory(String leaveCatetory) {
		this.leaveCatetory = leaveCatetory;
	}
}
