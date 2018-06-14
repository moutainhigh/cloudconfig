package com.kuangchi.sdd.elevatorConsole.elevatorReport.model;

import java.util.List;

public class TriggerEvent {
	Integer recordId;
	String recordTime;
    String dayInWeek;//星期几   1,2,3,4,5,6,7,  星期一---星期日
    String recordType;
	List<String> floorList;
	String floorStatus;
	String recordFlag;
	
	public Integer getRecordId() {
		return recordId;
	}
	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}
	public String getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}
	public String getDayInWeek() {
		return dayInWeek;
	}
	public void setDayInWeek(String dayInWeek) {
		this.dayInWeek = dayInWeek;
	}
	public String getRecordType() {
		return recordType;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	public List<String> getFloorList() {
		return floorList;
	}
	public void setFloorList(List<String> floorList) {
		this.floorList = floorList;
	}
	public String getFloorStatus() {
		return floorStatus;
	}
	public void setFloorStatus(String floorStatus) {
		this.floorStatus = floorStatus;
	}
	public String getRecordFlag() {
		return recordFlag;
	}
	public void setRecordFlag(String recordFlag) {
		this.recordFlag = recordFlag;
	}
	
	
}