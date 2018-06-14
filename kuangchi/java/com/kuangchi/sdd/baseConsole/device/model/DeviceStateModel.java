package com.kuangchi.sdd.baseConsole.device.model;

public class DeviceStateModel {
	private String stateId; // 状态Id
	private String deviceNum; // 设备编号
	private String deviceMac;
	private String lockState; // 锁状态
	private String doorState; // 门状态
	private String keyState; // 按键状态
	private String skidState; // 防撬状态
	private String fireState; // 消防状态
	private String updateTime; // 更新时间
	private String door_num;
	private String door_name;
	private String deviceName;
	private String local_ip_address; //设备本地IP

	
	
	

	public String getLocal_ip_address() {
		return local_ip_address;
	}

	public void setLocal_ip_address(String local_ip_address) {
		this.local_ip_address = local_ip_address;
	}

	public String getDoor_num() {
		return door_num;
	}

	public void setDoor_num(String door_num) {
		this.door_num = door_num;
	}

	public String getDoor_name() {
		return door_name;
	}

	public void setDoor_name(String door_name) {
		this.door_name = door_name;
	}

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public String getDeviceNum() {
		return deviceNum;
	}

	public void setDeviceNum(String deviceNum) {
		this.deviceNum = deviceNum;
	}

	public String getLockState() {
		return lockState;
	}

	public void setLockState(String lockState) {
		this.lockState = lockState;
	}

	public String getDoorState() {
		return doorState;
	}

	public void setDoorState(String doorState) {
		this.doorState = doorState;
	}

	public String getKeyState() {
		return keyState;
	}

	public void setKeyState(String keyState) {
		this.keyState = keyState;
	}

	public String getSkidState() {
		return skidState;
	}

	public void setSkidState(String skidState) {
		this.skidState = skidState;
	}

	public String getFireState() {
		return fireState;
	}

	public void setFireState(String fireState) {
		this.fireState = fireState;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
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

}
