package com.kuangchi.sdd.elevatorConsole.device.model;

public class TkDevAuthorityCardModel {
	private Integer id;
	private String card_num;
	private String object_num;
	private String floor_group_num;
	private String device_num;
	private String object_type;
	private String begin_valid_time;
	private String card_type;
	/*private String staff_num;
	public String getStaff_num() {
		return staff_num;
	}
	public void setStaff_num(String staff_num) {
		this.staff_num = staff_num;
	}*/
	public String getCard_type() {
		return card_type;
	}
	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
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
	public String getFloor_group_num() {
		return floor_group_num;
	}
	public void setFloor_group_num(String floor_group_num) {
		this.floor_group_num = floor_group_num;
	}
	public String getDevice_num() {
		return device_num;
	}
	public void setDevice_num(String device_num) {
		this.device_num = device_num;
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
	public Integer getDelete_flag() {
		return delete_flag;
	}
	public void setDelete_flag(Integer delete_flag) {
		this.delete_flag = delete_flag;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDevice_ip() {
		return device_ip;
	}
	public void setDevice_ip(String device_ip) {
		this.device_ip = device_ip;
	}
	public Integer getDevice_port() {
		return device_port;
	}
	public void setDevice_port(Integer device_port) {
		this.device_port = device_port;
	}
	private String end_valid_time;
	private Integer delete_flag;
	private String address;
	private String device_ip;
	private Integer device_port;
	private String floor_list;
	public String getFloor_list() {
		return floor_list;
	}
	public void setFloor_list(String floor_list) {
		this.floor_list = floor_list;
	}
	
}
