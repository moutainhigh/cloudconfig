package com.kuangchi.sdd.attendanceConsole.attendAnalysis.model;


public class AttendanceRateModel {
	
	private String total_rate;//总比率
	private String work_rate;//正常上班率
	private String over_rate;//加班率
	private String later_rate;//迟到率
	private String early_rate;//早退率
	private String kg_rate;//旷工率
	private String leave_rate;//休假率
	
	public String getWork_rate() {
		return work_rate;
	}
	public void setWork_rate(String work_rate) {
		this.work_rate = work_rate;
	}
	public String getOver_rate() {
		return over_rate;
	}
	public void setOver_rate(String over_rate) {
		this.over_rate = over_rate;
	}
	public String getLater_rate() {
		return later_rate;
	}
	public String getTotal_rate() {
		return total_rate;
	}
	public String getLeave_rate() {
		return leave_rate;
	}
	public void setLeave_rate(String leave_rate) {
		this.leave_rate = leave_rate;
	}
	public void setTotal_rate(String total_rate) {
		this.total_rate = total_rate;
	}
	public void setLater_rate(String later_rate) {
		this.later_rate = later_rate;
	}
	public String getEarly_rate() {
		return early_rate;
	}
	public void setEarly_rate(String early_rate) {
		this.early_rate = early_rate;
	}
	public String getKg_rate() {
		return kg_rate;
	}
	public void setKg_rate(String kg_rate) {
		this.kg_rate = kg_rate;
	}
	
}
