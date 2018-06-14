package com.kuangchi.sdd.ZigBeeConsole.record.model;


/**
 * @创建人　: 高育漫
 * @创建时间: 2016-8-18 上午9:34:04
 * @功能描述: 光子锁记录-model
 */
public class RecordModel {

	private int id; 
	private String device_id; //设备编号
	private String open_type;//开门类型
	private String light_id;//光ID
	private String staff_name; //人员姓名
	private String swipt_date; //刷卡时间
	private String room_num;//房间号
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public String getOpen_type() {
		return open_type;
	}
	public void setOpen_type(String open_type) {
		this.open_type = open_type;
	}
	public String getLight_id() {
		return light_id;
	}
	public void setLight_id(String light_id) {
		this.light_id = light_id;
	}
	public String getStaff_name() {
		return staff_name;
	}
	public void setStaff_name(String staff_name) {
		this.staff_name = staff_name;
	}
	public String getSwipt_date() {
		return swipt_date;
	}
	public void setSwipt_date(String swipt_date) {
		this.swipt_date = swipt_date;
	}
	public String getRoom_num() {
		return room_num;
	}
	public void setRoom_num(String room_num) {
		this.room_num = room_num;
	}
	
	
	
}
