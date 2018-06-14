package com.kuangchi.sdd.doorAccessConsole.authority.model;

public class DeviceModel {
     private String deviceNum;
     private String deviceName;
     private String deviceMac;
     private String localIpAddress;
     private String deciveGroupNum;
     private String groupName;
     
   
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getDeciveGroupNum() {
		return deciveGroupNum;
	}
	public void setDeciveGroupNum(String deciveGroupNum) {
		this.deciveGroupNum = deciveGroupNum;
	}
	public String getDeviceNum() {
		return deviceNum;
	}
	public void setDeviceNum(String deviceNum) {
		this.deviceNum = deviceNum;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDeviceMac() {
		return deviceMac;
	}
	public void setDeviceMac(String deviceMac) {
		this.deviceMac = deviceMac;
	}
	public String getLocalIpAddress() {
		return localIpAddress;
	}
	public void setLocalIpAddress(String localIpAddress) {
		this.localIpAddress = localIpAddress;
	}
	
     
}
