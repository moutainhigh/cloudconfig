package com.kuangchi.sdd.attendanceConsole.attendAnalysis.model;

public class ResultModel {
	
	private boolean isSuccess;
	private String msg;
	private boolean noData;
	private AttendanceRateModel attendanceRate;
	
	public boolean isNoData() {
		return noData;
	}
	public void setNoData(boolean noData) {
		this.noData = noData;
	}
	
	
	public AttendanceRateModel getAttendanceRate() {
		return attendanceRate;
	}
	public void setAttendanceRate(AttendanceRateModel attendanceRate) {
		this.attendanceRate = attendanceRate;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
