package com.kuangchi.sdd.elevatorConsole.device.model;

import java.util.ArrayList;
import java.util.List;

public class OpenTimeZone {
    Integer weekDay; //星期几   //格式为   01表示星期1   07表示星期日  08 表示节假日
    
    List<TimeZone>  timeZoneList=new ArrayList<TimeZone>();

	 

	public Integer getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(Integer weekDay) {
		this.weekDay = weekDay;
	}

	public List<TimeZone> getTimeZoneList() {
		return timeZoneList;
	}

	public void setTimeZoneList(List<TimeZone> timeZoneList) {
		this.timeZoneList = timeZoneList;
	}
    
}
