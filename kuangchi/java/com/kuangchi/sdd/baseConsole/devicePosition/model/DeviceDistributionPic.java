package com.kuangchi.sdd.baseConsole.devicePosition.model;

import java.util.List;

import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.consumeConsole.devicePosition.model.DevicePosi;
import com.kuangchi.sdd.elevatorConsole.devicePosition.model.TKDevicePosi;

public class DeviceDistributionPic {

	private String id; 
	private String description; //图片描述
	private String pic_path; //图片路径
	private String device_group_num; //设备组编号
	private String delete_flag; //删除状态  0：未删除  1：已删除
	private String create_time; //创建时间
	private List<String> legal_devices; //该图片下可存在的设备编号列表
	private List<DeviceInfo> exist_devices; //该图片下已存在的设备列表（门禁系统）
	private List<DevicePosi> exist_XFdevices;//该图片下已存在的设备列表（消费系统）
	private List<TKDevicePosi> exist_TKdevices;//该图片下已存在的设备列表（梯控系统）
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPic_path() {
		return pic_path;
	}
	public void setPic_path(String pic_path) {
		this.pic_path = pic_path;
	}
	public String getDevice_group_num() {
		return device_group_num;
	}
	public void setDevice_group_num(String device_group_num) {
		this.device_group_num = device_group_num;
	}
	public String getDelete_flag() {
		return delete_flag;
	}
	public void setDelete_flag(String delete_flag) {
		this.delete_flag = delete_flag;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public List<DeviceInfo> getExist_devices() {
		return exist_devices;
	}
	public void setExist_devices(List<DeviceInfo> exist_devices) {
		this.exist_devices = exist_devices;
	}
	public List<String> getLegal_devices() {
		return legal_devices;
	}
	public void setLegal_devices(List<String> legal_devices) {
		this.legal_devices = legal_devices;
	}
	public List<DevicePosi> getExist_XFdevices() {
		return exist_XFdevices;
	}
	public void setExist_XFdevices(List<DevicePosi> exist_XFdevices) {
		this.exist_XFdevices = exist_XFdevices;
	}
	public List<TKDevicePosi> getExist_TKdevices() {
		return exist_TKdevices;
	}
	public void setExist_TKdevices(List<TKDevicePosi> exist_TKdevices) {
		this.exist_TKdevices = exist_TKdevices;
	}
	
	
}
