package com.kuangchi.sdd.zigbee.model;

public class Data0xAA0x0D extends Data {
	String lockId; //锁ID   8个字节组成的16个十六进制的字符串
	public String getLockId() {
		return lockId;
	}
	public void setLockId(String lockId) {
		this.lockId = lockId;
	}
	
	
	
}
