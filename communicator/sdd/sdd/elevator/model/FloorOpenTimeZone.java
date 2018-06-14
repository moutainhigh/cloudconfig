package com.kuangchi.sdd.elevator.model;

import java.util.ArrayList;
import java.util.List;

public class FloorOpenTimeZone {
     Integer floor;
     List<TimeZone> timeZoneList=new ArrayList<TimeZone>();
	public Integer getFloor() {
		return floor;
	}
	public void setFloor(Integer floor) {
		this.floor = floor;
	}
	public List<TimeZone> getTimeZoneList() {
		return timeZoneList;
	}
	public void setTimeZoneList(List<TimeZone> timeZoneList) {
		this.timeZoneList = timeZoneList;
	}
     
     
     
     
}
