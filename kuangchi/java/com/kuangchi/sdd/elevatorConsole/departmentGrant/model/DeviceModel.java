package com.kuangchi.sdd.elevatorConsole.departmentGrant.model;

public class DeviceModel {
	private String id;
	private String device_num;
	private String device_name;
	private String device_ip;
	private String address;
	private Integer device_port;
	private String mac;
	private String online_state;
	private String create_time;
	private String description;
	
	
	public String getOnline_state() {
		return online_state;
	}
	public void setOnline_state(String online_state) {
		this.online_state = online_state;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
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
