package com.kuangchi.sdd.zigbee.model;

public class Data0xAA0x08 extends Data  {
   String lockId; //锁ID   8个字节组成的16个十六进制的字符串
   int status;//为1字节，十六进制，0x00表示更新成功；0x01表示失败
 
public String getLockId() {
	return lockId;
}
public void setLockId(String lockId) {
	this.lockId = lockId;
}
public int getStatus() {
	return status;
}
public void setStatus(int status) {
	this.status = status;
}
   
}