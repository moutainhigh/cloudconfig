package com.kuangchi.sdd.zigbee.model;

public class Data0xAA0x06 extends Data  {
	   String lockId; //锁ID   8个字节组成的16个十六进制的字符串
	   String bateryBalance; //电池电量，“5.1”，表示电池电压5.1伏
	   String roomNo;////4个字节的字符串
	 
	public String getLockId() {
		return lockId;
	}
	public void setLockId(String lockId) {
		this.lockId = lockId;
	}
	 
	public String getBateryBalance() {
		return bateryBalance;
	}
	public void setBateryBalance(String bateryBalance) {
		this.bateryBalance = bateryBalance;
	}
	public String getRoomNo() {
		return roomNo;
	}
	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
	 
	 
}
