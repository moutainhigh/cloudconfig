package com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.model;

/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-9-27 
 * @功能描述: 梯控设备model
 */
public class TkDeviceModel {
       private Integer id;              //设备ID
       private String  device_num;      //设备编号
       private String  device_name;     //设备名称
       private String  device_ip;       //设备IP
       private String  mac;             //MAC地址
       private String  online_state;    //设备在线状态
       private String  create_time;     //创建时间
       
       
	public Integer getId() {
		return id;
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
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getOnline_state() {
		return online_state;
	}
	public void setOnline_state(String online_state) {
		this.online_state = online_state;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
       
	
}
