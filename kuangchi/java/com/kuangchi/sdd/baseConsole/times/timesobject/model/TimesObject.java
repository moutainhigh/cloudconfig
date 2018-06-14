package com.kuangchi.sdd.baseConsole.times.timesobject.model;

import com.kuangchi.sdd.baseConsole.card.model.BaseModelSupport;
/**
 * @创建人　: 梁豆豆
 * @创建时间: 2016-4-5 下午4:57:01
 * @功能描述: 对象时间组Bean
 */
public class TimesObject extends BaseModelSupport{
	private Integer object_times_id;//对象时间组ID
	private String object_type;//对象类型(设备、节假日)
	private String object_num;//设备编号
	private String group_num;//时段组编号
	private String create_time;//创建时间
	private String result_code; //接口返回信息
	
	private String device_name; //设备名称
	private String group_name; //时段组名称
	private String device_mac; //设备mac地址
	private String device_ip; //设备ip地址
	
	
	
	
	public String getDevice_mac() {
		return device_mac;
	}
	public void setDevice_mac(String device_mac) {
		this.device_mac = device_mac;
	}
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public String getResult_code() {
		return result_code;
	}
	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}
	public Integer getObject_times_id() {
		return object_times_id;
	}
	public void setObject_times_id(Integer object_times_id) {
		this.object_times_id = object_times_id;
	}
	public String getObject_type() {
		return object_type;
	}
	public void setObject_type(String object_type) {
		this.object_type = object_type;
	}
	public String getObject_num() {
		return object_num;
	}
	public void setObject_num(String object_num) {
		this.object_num = object_num;
	}
	public String getGroup_num() {
		return group_num;
	}
	public void setGroup_num(String group_num) {
		this.group_num = group_num;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getDevice_ip() {
		return device_ip;
	}
	public void setDevice_ip(String device_ip) {
		this.device_ip = device_ip;
	}
	
	
}
