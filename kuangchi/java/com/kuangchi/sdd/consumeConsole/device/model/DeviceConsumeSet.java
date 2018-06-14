package com.kuangchi.sdd.consumeConsole.device.model;

public class DeviceConsumeSet {
	private String id;
	private String device_num;//设备编号
	private String meal_num;//餐次编号
	private String meal_name;
	private String times_limit;//限制次数
	private String create_time;//创建时间
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
	public String getMeal_num() {
		return meal_num;
	}
	public void setMeal_num(String meal_num) {
		this.meal_num = meal_num;
	}
	public String getTimes_limit() {
		return times_limit;
	}
	public void setTimes_limit(String times_limit) {
		this.times_limit = times_limit;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getMeal_name() {
		return meal_name;
	}
	public void setMeal_name(String meal_name) {
		this.meal_name = meal_name;
	}
	
}
