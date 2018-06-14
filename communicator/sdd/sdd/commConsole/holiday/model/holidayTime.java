package com.kuangchi.sdd.commConsole.holiday.model;

import com.kuangchi.sdd.comm.equipment.base.Data;

public class holidayTime extends Data {
	private String year;
	private String month;
	private String day;
	private String dayOfWeek;
	
	
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	
}
