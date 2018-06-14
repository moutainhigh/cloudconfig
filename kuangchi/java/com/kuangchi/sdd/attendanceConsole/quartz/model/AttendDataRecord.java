package com.kuangchi.sdd.attendanceConsole.quartz.model;

public class AttendDataRecord {
	Integer id;		  
	String staff_num; //员工编号
	String staff_no; //员工编号
	String staff_name;//员工姓名
	String checktime; //打卡时间
	String device_id; //刷卡设备ID
	String device_name;//刷卡设备名称
	String device_mac; //设备mac
	String door_num;   //刷卡门编号
	String door_name;  //刷卡门名称
	String flag_status;//是否补打卡   0否 1是
	String create_time;//创建时间
	
	
	public String getStaff_no() {
		return staff_no;
	}
	public void setStaff_no(String staff_no) {
		this.staff_no = staff_no;
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
	public String getStaff_name() {
		return staff_name;
	}
	public void setStaff_name(String staff_name) {
		this.staff_name = staff_name;
	}
	public String getChecktime() {
		return checktime;
	}
	public void setChecktime(String checktime) {
		this.checktime = checktime;
	}
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	public String getDevice_mac() {
		return device_mac;
	}
	public void setDevice_mac(String device_mac) {
		this.device_mac = device_mac;
	}
	public String getDoor_num() {
		return door_num;
	}
	public void setDoor_num(String door_num) {
		this.door_num = door_num;
	}
	public String getDoor_name() {
		return door_name;
	}
	public void setDoor_name(String door_name) {
		this.door_name = door_name;
	}
	public String getFlag_status() {
		return flag_status;
	}
	public void setFlag_status(String flag_status) {
		this.flag_status = flag_status;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	
}
