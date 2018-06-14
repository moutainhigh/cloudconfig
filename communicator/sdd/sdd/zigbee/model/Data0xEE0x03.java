package com.kuangchi.sdd.zigbee.model;

public class Data0xEE0x03  extends Data {
     String lockId; //锁ID   8个字节组成的16个十六进制的字符串
     long photonId;//光Id  4 个字节
     int transportFlag; //为1个字节，十六进制，如0x01，还有下一组数据要传输；0x00，后续没有数据要传输
     
     
     
     
	public String getLockId() {
		return lockId;
	}

	public void setLockId(String lockId) {
		this.lockId = lockId;
	}

	 

	public long getPhotonId() {
		return photonId;
	}

	public void setPhotonId(long photonId) {
		this.photonId = photonId;
	}

	public int getTransportFlag() {
		return transportFlag;
	}

	public void setTransportFlag(int transportFlag) {
		this.transportFlag = transportFlag;
	}

	 
     
     
}
