package com.kuangchi.sdd.zigbee.model;

public class Data0xAA0x09  extends Data {
    String zigBeeId; //8字节,可以是锁ID，也可以是Zigbee网关上Zigbee芯片的ID。为8字节，十六进制，如格式：0x0102030405060708
    int status;//为1字节，十六进制，0x00表示设置成功；0x01表示失败
	 
	public String getZigBeeId() {
		return zigBeeId;
	}
	public void setZigBeeId(String zigBeeId) {
		this.zigBeeId = zigBeeId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
    
}
