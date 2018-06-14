package com.kuangchi.sdd.zigbee.model;

public class Data0xAA0x01 extends Data {
    String lockId ;  //锁ID   8个字节组成的16个十六进制的字符串
    String roomNo;//4个字节的字符串
	
    String firmwareVersion; // 固件版本号
    String softwareVersion; // 软件版本号
 
	public String getRoomNo() {
		return roomNo;
	}
	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
	public String getLockId() {
		return lockId;
	}
	public void setLockId(String lockId) {
		this.lockId = lockId;
	}
	public String getFirmwareVersion() {
		return firmwareVersion;
	}
	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}
	public String getSoftwareVersion() {
		return softwareVersion;
	}
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}
	
	
	
	
    
	
    
}
