package com.kuangchi.sdd.baseConsole.count.model;

/**
 * @创建人　: chudan.guo
 * @创建时间: 2016-9-28 
 * @功能描述: 报警事件统计-model
 */
public class CountWarining {
	private String card_num; 	//卡号	
	private String time;		//报警时间
	private String device_num;	//设备编号
	private String door_num;	//门编号
	private String od_type;	  //开门类型
	
	private String device_mac;//本地mac地址
	private String local_ip_address;//本地ip地址
	private String device_name;//设备名称
	private String door_name;//门名称
	
	
	
	public String getDevice_mac() {
		return device_mac;
	}
	public void setDevice_mac(String device_mac) {
		this.device_mac = device_mac;
	}
	public String getCard_num() {
		return card_num;
	}
	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
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
	public String getLocal_ip_address() {
		return local_ip_address;
	}
	public void setLocal_ip_address(String local_ip_address) {
		this.local_ip_address = local_ip_address;
	}
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	public String getDoor_name() {
		return door_name;
	}
	public void setDoor_name(String door_name) {
		this.door_name = door_name;
	}
	
	
	
	
}
