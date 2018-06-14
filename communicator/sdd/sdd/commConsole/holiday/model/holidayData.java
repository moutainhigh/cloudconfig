package com.kuangchi.sdd.commConsole.holiday.model;

import java.util.List;

public class holidayData {
	private List<holidayTime> holiday;
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

	public List<holidayTime> getHoliday() {
		return holiday;
	}

	public void setHoliday(List<holidayTime> holiday) {
		this.holiday = holiday;
	}
	
}
