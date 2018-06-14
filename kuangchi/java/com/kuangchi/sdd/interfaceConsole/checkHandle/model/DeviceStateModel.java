package com.kuangchi.sdd.interfaceConsole.checkHandle.model;


import java.util.List;

import com.kuangchi.sdd.baseConsole.event.model.DeviceEventWarningModel;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-5-19 下午2:25:32
 * @功能描述: 接口所需 设备状态bean
 */
public class DeviceStateModel {

	private String lock_state;//锁状态
	private String door_state;//门状态
	private String key_state;//按键状态
	private String skid_state;//防撬状态
	private String fire_state;//消防状态
	private String update_time;//更新时间
	private List<Long> events;//事件信息
	
	
	public String getLock_state() {
		return lock_state;
	}
	public void setLock_state(String lock_state) {
		this.lock_state = lock_state;
	}
	public String getDoor_state() {
		return door_state;
	}
	public void setDoor_state(String door_state) {
		this.door_state = door_state;
	}
	public String getKey_state() {
		return key_state;
	}
	public void setKey_state(String key_state) {
		this.key_state = key_state;
	}
	public String getSkid_state() {
		return skid_state;
	}
	public void setSkid_state(String skid_state) {
		this.skid_state = skid_state;
	}
	public String getFire_state() {
		return fire_state;
	}
	public void setFire_state(String fire_state) {
		this.fire_state = fire_state;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public List<Long> getEvents() {
		return events;
	}
	public void setEvent(List<Long> events) {
		this.events = events;
	}
	
}
