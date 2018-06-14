package com.kuangchi.sdd.baseConsole.countattendance.model;

import com.kuangchi.sdd.baseConsole.card.model.BaseModelSupport;

public class CountAttendance extends BaseModelSupport{
	private String attendance_id;//门禁卡记录信息ID
private String card_num;//卡号
private String staff_name;//员工姓名
private String attendance_time;//考勤时间
private String device_ip;//设备ip
private String device_num;//设备编号
private String door_num;//门编号
private String od_type;//开门类型
private String log_type;// 日志记录类型  
private String staff_no; // 员工工号
private String dept_name; //部门名称


public String getStaff_no() {
	return staff_no;
}
public void setStaff_no(String staff_no) {
	this.staff_no = staff_no;
}
public String getDept_name() {
	return dept_name;
}
public void setDept_name(String dept_name) {
	this.dept_name = dept_name;
}
public String getStaff_name() {
	return staff_name;
}
public void setStaff_name(String staff_name) {
	this.staff_name = staff_name;
}
public String getAttendance_id() {
	return attendance_id;
}
public void setAttendance_id(String attendance_id) {
	this.attendance_id = attendance_id;
}
public String getCard_num() {
	return card_num;
}
public void setCard_num(String card_num) {
	this.card_num = card_num;
}
public String getAttendance_time() {
	return attendance_time;
}
public void setAttendance_time(String attendance_time) {
	this.attendance_time = attendance_time;
}
public String getDevice_ip() {
	return device_ip;
}
public void setDevice_ip(String device_ip) {
	this.device_ip = device_ip;
}
public String getDevice_num() {
	return device_num;
}
public void setDevice_num(String device_num) {
	this.device_num = device_num;
}
public String getDoor_num() {
	return door_num;
}
public void setDoor_num(String door_num) {
	this.door_num = door_num;
}
public String getOd_type() {
	return od_type;
}
public void setOd_type(String od_type) {
	this.od_type = od_type;
}
public String getLog_type() {
	return log_type;
}
public void setLog_type(String log_type) {
	this.log_type = log_type;
}


}
