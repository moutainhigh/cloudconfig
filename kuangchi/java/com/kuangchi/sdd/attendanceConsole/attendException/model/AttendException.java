package com.kuangchi.sdd.attendanceConsole.attendException.model;

import com.kuangchi.sdd.baseConsole.card.model.BaseModelSupport;

public class AttendException extends BaseModelSupport{
private Integer id;//考勤异常信息ID
private String staff_num;//员工编号
private String staff_no;//员工编号
private String staff_email;//员工邮件地址
public String getStaff_email() {
	return staff_email;
}
public void setStaff_email(String staff_email) {
	this.staff_email = staff_email;
}
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
public String getStaff_no() {
	return staff_no;
}
public void setStaff_no(String staff_no) {
	this.staff_no = staff_no;
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
public String getTime_interval() {
	return time_interval;
}
public void setTime_interval(String time_interval) {
	this.time_interval = time_interval;
}
public String getException_type() {
	return exception_type;
}
public void setException_type(String exception_type) {
	this.exception_type = exception_type;
}
public String getDeal_state() {
	return deal_state;
}
public void setDeal_state(String deal_state) {
	this.deal_state = deal_state;
}
private String staff_name;//员工姓名
private String everyday_time;//每天时间
private String time_point;//时间点，如缺卡时间，早退时间等，格式如2010-1-23 12:3:14
private String duty_time_type;//1.上午上班，2上午下班，3下午上班，4下午下班
public String getDuty_type() {
	return duty_type;
}
public void setDuty_type(String duty_type) {
	this.duty_type = duty_type;
}
private String time_interval;//迟到时间或早退时间或旷工时间，单位：分钟
private String exception_type;//1迟到，2早退，3缺卡
private String deal_state;//处理状态：0 未处理  1 已处理
private String duty_type;
}
