package com.kuangchi.sdd.attendanceConsole.statistic.model;

public class AttendanceException {
	   Integer id;
	  String staff_num;//'员工编号',
	  String staff_name;// '员工姓名',
	  String everyday_time;// '每天时间',
	  String time_point;// '时间点,如缺卡时间,早退时间等，格式如 2012-01-01 10:10:10',
	  String duty_time_type;// '1 上午上班 ，2 上午下班，3下午上班，4 下午下班',
	  Integer time_interval;// '迟到时间或早退时间或旷工时间等 ，单位分钟',
	  String exception_type;// '1 迟到 2 早退  3 缺卡',
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStaff_num() {
		return staff_num;
	}
	public void setStaff_num(String staff_num) {
		this.staff_num = staff_num;
	}
	public String getStaff_name() {
		return staff_name;
	}
	public void setStaff_name(String staff_name) {
		this.staff_name = staff_name;
	}
	public String getEveryday_time() {
		return everyday_time;
	}
	public void setEveryday_time(String everyday_time) {
		this.everyday_time = everyday_time;
	}
	public String getTime_point() {
		return time_point;
	}
	public void setTime_point(String time_point) {
		this.time_point = time_point;
	}
	public String getDuty_time_type() {
		return duty_time_type;
	}
	public void setDuty_time_type(String duty_time_type) {
		this.duty_time_type = duty_time_type;
	}

	public Integer getTime_interval() {
		return time_interval;
	}
	public void setTime_interval(Integer time_interval) {
		this.time_interval = time_interval;
	}
	public String getException_type() {
		return exception_type;
	}
	public void setException_type(String exception_type) {
		this.exception_type = exception_type;
	}
	    
}
