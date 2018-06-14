package com.kuangchi.sdd.zigbee.model;

public class Data0xEE0x08  extends Data {
  String lockId; //锁ID   8个字节组成的16个十六进制的字符串
  long time; // 时间  4字节 单位秒
 
public String getLockId() {
	return lockId;
}
public void setLockId(String lockId) {
	this.lockId = lockId;
}
public long getTime() {
	return time;
}
public void setTime(long time) {
	this.time = time;
}
  
}
