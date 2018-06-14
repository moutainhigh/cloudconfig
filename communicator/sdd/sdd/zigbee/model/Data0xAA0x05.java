package com.kuangchi.sdd.zigbee.model;

public class Data0xAA0x05 extends Data  {
     String lockId; //锁ID   8个字节组成的16个十六进制的字符串
     String panId;//2 字节
     String roomNo;//4个字节的字符串
     String bateryBalance; //电池电量，“5.1”，表示电池电压5.1伏
     int signalLevel; //信号强度
     long time;   //时间戳
     int communityNo;
 
	public String getLockId() {
		return lockId;
	}
	public void setLockId(String lockId) {
		this.lockId = lockId;
	}
	 
	 
	public String getPanId() {
		return panId;
	}
	public void setPanId(String panId) {
		this.panId = panId;
	}
	public String getRoomNo() {
		return roomNo;
	}
	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
 
	public String getBateryBalance() {
		return bateryBalance;
	}
	public void setBateryBalance(String bateryBalance) {
		this.bateryBalance = bateryBalance;
	}
	public int getSignalLevel() {
		return signalLevel;
	}
	public void setSignalLevel(int signalLevel) {
		this.signalLevel = signalLevel;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getCommunityNo() {
		return communityNo;
	}
	public void setCommunityNo(int communityNo) {
		this.communityNo = communityNo;
	}
	
	
	
     
}
