package com.xkd.model;

import java.io.Serializable;

public class ScheduleUser implements Serializable {

	/**
	 * 同行人员表Id，
	 */
	private String id;
	
	/**
	 * 行程Id
	 * 
	 */
	private String scheduleId;
	/**
	 * 同行人员老师Id
	 */
	private String userId;
	
	private String readStatus;
	
	private String status;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getReadStatus() {
		return readStatus;
	}
	public void setReadStatus(String readStatus) {
		this.readStatus = readStatus;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	
	
}
