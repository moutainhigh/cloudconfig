package com.kuangchi.sdd.attendanceConsole.attendCount.model;

public class LeaveTimeModel {
	private String leaveSTime;
	private String leaveETime;
	private String reason;
	private String leaveCatetory;
	
	
	public String getLeaveCatetory() {
		return leaveCatetory;
	}
	public void setLeaveCatetory(String leaveCatetory) {
		this.leaveCatetory = leaveCatetory;
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
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
}
