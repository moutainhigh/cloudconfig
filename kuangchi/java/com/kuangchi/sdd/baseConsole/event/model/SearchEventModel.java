package com.kuangchi.sdd.baseConsole.event.model;
/*
 *huixian.pan 查询字段Model 
 * */
public class SearchEventModel {
	private String deviceName;//设备名称
	private String cardNum;//卡编号
	private String doorName; //门名称
	private String startDate;//开始日期
	private String endDate;//结束日期
	
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getDoorName() {
		return doorName;
	}
	public void setDoorName(String doorName) {
		this.doorName = doorName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
}
