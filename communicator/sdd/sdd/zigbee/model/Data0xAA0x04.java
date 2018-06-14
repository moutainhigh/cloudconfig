package com.kuangchi.sdd.zigbee.model;

public class Data0xAA0x04  extends Data {
    String lockId; //锁ID   8个字节组成的16个十六进制的字符串
    int openType;//1字节,开锁类型 0x01代表光ID；0x02代表密码
	long photonId;//光ID 4 字节组成的8个十 六十制字符串 
	public String getLockId() {
		return lockId;
	}
	public void setLockId(String lockId) {
		this.lockId = lockId;
	}
	public int getOpenType() {
		return openType;
	}
	public void setOpenType(int openType) {
		this.openType = openType;
	}
	public long getPhotonId() {
		return photonId;
	}
	public void setPhotonId(long photonId) {
		this.photonId = photonId;
	}
	
	
}
