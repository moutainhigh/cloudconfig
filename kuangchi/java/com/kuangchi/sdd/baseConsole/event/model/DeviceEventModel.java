package com.kuangchi.sdd.baseConsole.event.model;

public class DeviceEventModel {
	private Integer eventId ; //id 主键
	private String eventType;  //事件类型
	private String deviceNum;  //设备编号
	private String doorNum; //门编号
	private String cardNum;//卡编号
	private String eventDm;//事件代码
	private String eventDesc;//事件描述
	private String eventDate;//事件日期
	
	private String doorName; //门名称
	private String deviceName;//设备名称
	private String eventName;//事件名称
	private String deviceMac;//设备MAC地址
	private String staffName;
	private String staffNo;
	private String local_ip_address;//设备本地地址
	
	
	public String getLocal_ip_address() {
		return local_ip_address;
	}
	public void setLocal_ip_address(String local_ip_address) {
		this.local_ip_address = local_ip_address;
	}
	public String getDeviceMac() {
		return deviceMac;
	}
	public void setDeviceMac(String deviceMac) {
		this.deviceMac = deviceMac;
	}
	public Integer getEventId() {
		return eventId;
	}
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getDeviceNum() {
		return deviceNum;
	}
	public void setDeviceNum(String deviceNum) {
		this.deviceNum = deviceNum;
	}
	public String getDoorNum() {
		return doorNum;
	}
	public void setDoorNum(String doorNum) {
		this.doorNum = doorNum;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getEventDm() {
		return eventDm;
	}
	public void setEventDm(String eventDm) {
		this.eventDm = eventDm;
	}
	public String getEventDesc() {
		return eventDesc;
	}
	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}
	public String getEventDate() {
		return eventDate;
	}
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}
	public String getDoorName() {
		return doorName;
	}
	public void setDoorName(String doorName) {
		this.doorName = doorName;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public String getStaffNo() {
		return staffNo;
	}
	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}
		
}
