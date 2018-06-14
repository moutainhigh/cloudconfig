package com.kuangchi.sdd.elevatorConsole.devicePosition.model;

/**
 * 设备地图坐标
 * @author minting.he
 *
 */
public class TKDevicePosi {

	private String device_num;		//设备编号
	private String mac;				//设备mac地址
	private String device_group_num;//设备组编号
	private Double x_position;		//X坐标
	private Double y_position;		//Y坐标
	private String online_state;	//在线状态 0 在线，1 离线
	private String pic_id;			//地图图片id
	
	public String getDevice_num() {
		return device_num;
	}
	public void setDevice_num(String device_num) {
		this.device_num = device_num;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getDevice_group_num() {
		return device_group_num;
	}
	public void setDevice_group_num(String device_group_num) {
		this.device_group_num = device_group_num;
	}
	public Double getX_position() {
		return x_position;
	}
	public void setX_position(Double x_position) {
		this.x_position = x_position;
	}
	public Double getY_position() {
		return y_position;
	}
	public void setY_position(Double y_position) {
		this.y_position = y_position;
	}
	public String getOnline_state() {
		return online_state;
	}
	public void setOnline_state(String online_state) {
		this.online_state = online_state;
	}
	public String getPic_id() {
		return pic_id;
	}
	public void setPic_id(String pic_id) {
		this.pic_id = pic_id;
	}
	
	
}
