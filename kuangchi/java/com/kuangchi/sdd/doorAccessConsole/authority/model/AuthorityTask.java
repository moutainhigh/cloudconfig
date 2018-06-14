package com.kuangchi.sdd.doorAccessConsole.authority.model;

public class AuthorityTask {
	private String card_num;
	private String door_num;
	private String device_num;
	private String device_mac;
	private String device_type;
	private String valid_start_time;
	private String valid_end_time;
	private String time_group_num;
	private String try_times;
	private String flag;	// 0 授权， 1 删除
	private String create_time;
	
	public String getCard_num() {
		return card_num;
	}
	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}
	public String getDoor_num() {
		return door_num;
	}
	public void setDoor_num(String door_num) {
		this.door_num = door_num;
	}
	public String getDevice_num() {
		return device_num;
	}
	public void setDevice_num(String device_num) {
		this.device_num = device_num;
	}
	public String getDevice_mac() {
		return device_mac;
	}
	public void setDevice_mac(String device_mac) {
		this.device_mac = device_mac;
	}
	public String getDevice_type() {
		return device_type;
	}
	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}
	public String getValid_start_time() {
		return valid_start_time;
	}
	public void setValid_start_time(String valid_start_time) {
		this.valid_start_time = valid_start_time;
	}
	public String getValid_end_time() {
		return valid_end_time;
	}
	public void setValid_end_time(String valid_end_time) {
		this.valid_end_time = valid_end_time;
	}
	public String getTime_group_num() {
		return time_group_num;
	}
	public void setTime_group_num(String time_group_num) {
		this.time_group_num = time_group_num;
	}
	public String getTry_times() {
		return try_times;
	}
	public void setTry_times(String try_times) {
		this.try_times = try_times;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

   
}
