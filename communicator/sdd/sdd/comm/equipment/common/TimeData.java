package com.kuangchi.sdd.comm.equipment.common;

import com.kuangchi.sdd.comm.equipment.base.Data;

/**
 * 0xYYYYMMDDhhmmssWW： BCD格式表示时间。 //如：0x2005080409193005 表示2005年8月4日星期四9时19分30秒。
 * 注WW取值：星期日取0x01，星期六取值0x07。
 * 
 * @author yu.yao
 * 
 */

public class TimeData extends Data {
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private int second;
	private int dayOfWeek;

	public int getCrcFromSum() {
		int yearSum = 0;
		if (year > 255) {
			int year = getYear();
			short y1 = (short) (year >> 8);// 低位
			short y2 = (short) (year & 0x00ff);// 高位
			yearSum+=y1+y2;
		}else{
			yearSum = getYear();
		}

		return yearSum+ getMonth() + getDay() + getHour() + getMinute()
				+ getSecond() + getDayOfWeek();
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
}
