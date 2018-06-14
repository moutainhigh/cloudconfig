package com.kuangchi.sdd.consumeConsole.deviceType.model;

import com.kuangchi.sdd.base.model.BaseModelSupport;

public class DeviceType extends BaseModelSupport  {
	private String id;
	private String device_type_num;
	private String device_type_name;
	private String delete_flag;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDevice_type_num() {
		return device_type_num;
	}
	public void setDevice_type_num(String device_type_num) {
		this.device_type_num = device_type_num;
	}
	public String getDevice_type_name() {
		return device_type_name;
	}
	public void setDevice_type_name(String device_type_name) {
		this.device_type_name = device_type_name;
	}
	public String getDelete_flag() {
		return delete_flag;
	}
	public void setDelete_flag(String delete_flag) {
		this.delete_flag = delete_flag;
	}
	
}
