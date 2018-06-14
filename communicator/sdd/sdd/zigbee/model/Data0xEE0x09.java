package com.kuangchi.sdd.zigbee.model;

public class Data0xEE0x09  extends Data {
    String zigBeeId;  //8字节
    String panId;  //2字节
	 
	public String getZigBeeId() {
		return zigBeeId;
	}
	public void setZigBeeId(String zigBeeId) {
		this.zigBeeId = zigBeeId;
	}
	public String getPanId() {
		return panId;
	}
	public void setPanId(String panId) {
		this.panId = panId;
	}
	 
    
}
