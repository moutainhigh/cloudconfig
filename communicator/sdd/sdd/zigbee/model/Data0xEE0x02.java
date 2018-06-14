package com.kuangchi.sdd.zigbee.model;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class Data0xEE0x02 extends Data  {
     String lockId; //锁ID   8个字节组成的16个十六进制的字符串
     long photonId;  //光ID 4字节 4个字节组成的8个十六进制的字符串 如"11223344"
     long startTime;   //开始时间  4字节 单位秒
     long endTime;    //结束时间 4字节单位秒
     int transportFlag;  //为1个字节，十六进制，如0x01，还有下一组数据要传输；0x00，后续没有数据要传输
	 
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
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public int getTransportFlag() {
		return transportFlag;
	}
	public void setTransportFlag(int transportFlag) {
		this.transportFlag = transportFlag;
	}
     
     
}

