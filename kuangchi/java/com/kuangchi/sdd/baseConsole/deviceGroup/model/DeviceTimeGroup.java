package com.kuangchi.sdd.baseConsole.deviceGroup.model;

public class DeviceTimeGroup {

	private Integer id;
	private String device_num;	//设备编号
	private String door_num;	//门编号
	private Integer time_order; //时间编号 1 2 3 4 5 6 7 8 分别表示时间1，时间2，时间3，时间4，时间5，时间6，时间7，时间8
	private String sunday_time; //时间点格式
	private String monday_time;
	private String tuesday_time;
	private String wedsday_time;
	private String thursday_time;
	private String friday_time;
	private String saturday_time;
	private String vacation_time;
	private String sunday_action_type;//动作类型 0 不动作 1 自动模式 2 常开模式 3 常闭模式 4 触发模式
	private String monday_action_type;
	private String tuesday_action_type;
	private String wedsday_action_type;
	private String thursday_action_type;
	private String friday_action_type;
	private String saturday_action_type;
	private String vacation_action_type;
	private String validity_flag;//有效标志，0 有效 ，1 无效
	private String create_user;	 //创建人员代码
	private String create_time;	 //创建时间
	private String description;	 //描述
	
	private Integer device_id;   //设备自增id
	private String  device_type; //设备类型
	private String  door_name;	 //门名称
	private String  device_mac;  //设备Mac地址
	
	
	public String getDevice_mac() {
		return device_mac;
	}
	public void setDevice_mac(String device_mac) {
		this.device_mac = device_mac;
	}
	public Integer getId() {
		return id;
	}
	public Integer getDevice_id() {
		return device_id;
	}
	public void setDevice_id(Integer device_id) {
		this.device_id = device_id;
	}
	public String getDevice_type() {
		return device_type;
	}
	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}
	public String getDoor_name() {
		return door_name;
	}
	public void setDoor_name(String door_name) {
		this.door_name = door_name;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public Integer getTime_order() {
		return time_order;
	}
	public void setTime_order(Integer time_order) {
		this.time_order = time_order;
	}
	public String getSunday_time() {
		return sunday_time;
	}
	public void setSunday_time(String sunday_time) {
		this.sunday_time = sunday_time;
	}
	public String getMonday_time() {
		return monday_time;
	}
	public void setMonday_time(String monday_time) {
		this.monday_time = monday_time;
	}
	public String getTuesday_time() {
		return tuesday_time;
	}
	public void setTuesday_time(String tuesday_time) {
		this.tuesday_time = tuesday_time;
	}
	public String getWedsday_time() {
		return wedsday_time;
	}
	public void setWedsday_time(String wedsday_time) {
		this.wedsday_time = wedsday_time;
	}
	public String getThursday_time() {
		return thursday_time;
	}
	public void setThursday_time(String thursday_time) {
		this.thursday_time = thursday_time;
	}
	public String getFriday_time() {
		return friday_time;
	}
	public void setFriday_time(String friday_time) {
		this.friday_time = friday_time;
	}
	public String getSaturday_time() {
		return saturday_time;
	}
	public void setSaturday_time(String saturday_time) {
		this.saturday_time = saturday_time;
	}
	public String getVacation_time() {
		return vacation_time;
	}
	public void setVacation_time(String vacation_time) {
		this.vacation_time = vacation_time;
	}
	public String getSunday_action_type() {
		return sunday_action_type;
	}
	public void setSunday_action_type(String sunday_action_type) {
		this.sunday_action_type = sunday_action_type;
	}
	public String getMonday_action_type() {
		return monday_action_type;
	}
	public void setMonday_action_type(String monday_action_type) {
		this.monday_action_type = monday_action_type;
	}
	public String getTuesday_action_type() {
		return tuesday_action_type;
	}
	public void setTuesday_action_type(String tuesday_action_type) {
		this.tuesday_action_type = tuesday_action_type;
	}
	public String getWedsday_action_type() {
		return wedsday_action_type;
	}
	public void setWedsday_action_type(String wedsday_action_type) {
		this.wedsday_action_type = wedsday_action_type;
	}
	public String getThursday_action_type() {
		return thursday_action_type;
	}
	public void setThursday_action_type(String thursday_action_type) {
		this.thursday_action_type = thursday_action_type;
	}
	public String getFriday_action_type() {
		return friday_action_type;
	}
	public void setFriday_action_type(String friday_action_type) {
		this.friday_action_type = friday_action_type;
	}
	public String getSaturday_action_type() {
		return saturday_action_type;
	}
	public void setSaturday_action_type(String saturday_action_type) {
		this.saturday_action_type = saturday_action_type;
	}
	public String getVacation_action_type() {
		return vacation_action_type;
	}
	public void setVacation_action_type(String vacation_action_type) {
		this.vacation_action_type = vacation_action_type;
	}
	public String getValidity_flag() {
		return validity_flag;
	}
	public void setValidity_flag(String validity_flag) {
		this.validity_flag = validity_flag;
	}
	public String getCreate_user() {
		return create_user;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
