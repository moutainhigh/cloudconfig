package com.kuangchi.sdd.zigbee.model;

public class Data0xAA0x0A  extends Data {
    String zigBeeId;  //是Zigbee网关上Zigbee芯片的ID。为8字节，十六进制，如格式：0x0102030405060708
    String panId;//为2字节
 
    String firmwareVersion; // 固件版本号
    String softwareVersion; // 软件版本号
       
       
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
