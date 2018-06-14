package com.kuangchi.sdd.elevatorConsole.device.model;

import java.util.List;

public class FloorOpenTimeZone {

	private Integer floor;
	private List<TimeZone> timeZoneList;
	
	public Integer getFloor() {
		return floor;
	}
	public void setFloor(Integer floor) {
		this.floor = floor;
	}
	public List<TimeZone> getTimeList() {
		return timeZoneList;
	}
	public void setTimeList(List<TimeZone> timeZoneList) {
		this.timeZoneList = timeZoneList;
	}
	
	
}
