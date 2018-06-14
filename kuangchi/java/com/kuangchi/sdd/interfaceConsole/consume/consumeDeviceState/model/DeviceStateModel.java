package com.kuangchi.sdd.interfaceConsole.consume.consumeDeviceState.model;

public class DeviceStateModel {
	public String device_num;//设备编号
	public int online_state; //在线状态 0 离线 1 在线
	public int busy_state; //忙碌状态 0 空闲 1繁忙
	public int getOnline_state() {
		return online_state;
	}
	public void setOnline_state(int online_state) {
		this.online_state = online_state;
	}
	public int getBusy_state() {
		return busy_state;
	}
	public void setBusy_state(int busy_state) {
		this.busy_state = busy_state;
	}
	
}
