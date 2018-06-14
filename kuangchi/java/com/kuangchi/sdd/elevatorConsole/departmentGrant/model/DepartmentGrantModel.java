package com.kuangchi.sdd.elevatorConsole.departmentGrant.model;

public class DepartmentGrantModel {
	private String id;
	private String card_num;//卡编号
	private String card_type;//卡类型
	private String object_num;//对象编号，机构或人员编号
	private String object_name;//机构名称（部门名称）
	private String floor_group_num;//楼层组编号
	private String floor_group_name;//楼层组名称
	private String floor_list;//楼层
	private String device_num;//设备编号
	private String device_name;//设备名称
	private String device_ip;//设备ip
	private String address;//设备地址
	private Integer device_port;//设备端口
	private String object_type;//对象类型，0机构，1人员
	private String begin_valid_time;//开始日期
	private String end_valid_time;//结束日期
	
	private String task_state;
	private String online_state;
	
	
	
	public String getOnline_state() {
		return online_state;
	}
	public void setOnline_state(String online_state) {
		this.online_state = online_state;
	}
	public String getTask_state() {
		return task_state;
	}
	public void setTask_state(String task_state) {
		this.task_state = task_state;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	private String delete_flag;//删除标志
	
	private String mac;
	
	
	
	public String getCard_type() {
		return card_type;
	}
	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}
	public String getFloor_list() {
		return floor_list;
	}
	public void setFloor_list(String floor_list) {
		this.floor_list = floor_list;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCard_num() {
		return card_num;
	}
	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}
	public String getObject_num() {
		return object_num;
	}
	public void setObject_num(String object_num) {
		this.object_num = object_num;
	}
	public String getObject_name() {
		return object_name;
	}
	public void setObject_name(String object_name) {
		this.object_name = object_name;
	}
	public String getFloor_group_num() {
		return floor_group_num;
	}
	public void setFloor_group_num(String floor_group_num) {
		this.floor_group_num = floor_group_num;
	}
	public String getFloor_group_name() {
		return floor_group_name;
	}
	public void setFloor_group_name(String floor_group_name) {
		this.floor_group_name = floor_group_name;
	}
	public String getDevice_num() {
		return device_num;
	}
	public void setDevice_num(String device_num) {
		this.device_num = device_num;
	}
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	public String getDevice_ip() {
		return device_ip;
	}
	public void setDevice_ip(String device_ip) {
		this.device_ip = device_ip;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getDevice_port() {
		return device_port;
	}
	public void setDevice_port(Integer device_port) {
		this.device_port = device_port;
	}
	public String getObject_type() {
		return object_type;
	}
	public void setObject_type(String object_type) {
		this.object_type = object_type;
	}
	public String getBegin_valid_time() {
		return begin_valid_time;
	}
	public void setBegin_valid_time(String begin_valid_time) {
		this.begin_valid_time = begin_valid_time;
	}
	public String getEnd_valid_time() {
		return end_valid_time;
	}
	public void setEnd_valid_time(String end_valid_time) {
		this.end_valid_time = end_valid_time;
	}
	public String getDelete_flag() {
		return delete_flag;
	}
	public void setDelete_flag(String delete_flag) {
		this.delete_flag = delete_flag;
	}
}
