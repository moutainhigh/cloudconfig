package com.kuangchi.sdd.comm.equipment.base.service.model;


public class DeviceInfo2 {
	private String broadcastIP;//广播IP
	private String equipmentIP;// 设备IP地址
	private int equipmentRecordPort;// 记录上报端口
	//private int broadcastPort;//广播端口
	




	public String getBroadcastIP() {
		return broadcastIP;
	}

	public void setBroadcastIP(String broadcastIP) {
		this.broadcastIP = broadcastIP;
	}



	public String getEquipmentIP() {
		return equipmentIP;
	}

	public void setEquipmentIP(String equipmentIP) {
		this.equipmentIP = equipmentIP;
	}

	

	public int getEquipmentRecordPort() {
		return equipmentRecordPort;
	}

	public void setEquipmentRecordPort(int equipmentRecordPort) {
		this.equipmentRecordPort = equipmentRecordPort;
	}

/*	public int getBroadcastPort() {
		return broadcastPort;
	}

	public void setBroadcastPort(int broadcastPort) {
		this.broadcastPort = broadcastPort;
	}*/




}
