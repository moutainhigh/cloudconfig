package com.kuangchi.sdd.baseConsole.deviceTimes.holidaySetUp.model;

import java.io.Serializable;

import com.kuangchi.sdd.baseConsole.card.model.BaseModelSupport;


public class HolidaySetUp extends BaseModelSupport implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String holiday_times_num;//节假日时段编号
	private String holiday_date;	 //节假日日期 格式 yyyy-MM-dd
	private String day_of_week;		 //节假日星期 0,1,2,3,4,5,6,7分别表示：节假日，周一，周二，周三，周四，周五，周六，周日
	private String device_num;       //设备编号
	private String begin_time;
	private String end_time;
	
	public String getDevice_num() {
		return device_num;
	}
	public void setDevice_num(String device_num) {
		this.device_num = device_num;
	}
	public String getBegin_time() {
		return begin_time;
	}
	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getHoliday_times_num() {
		return holiday_times_num;
	}
	public void setHoliday_times_num(String holiday_times_num) {
		this.holiday_times_num = holiday_times_num;
	}
	public String getHoliday_date() {
		return holiday_date;
	}
	public void setHoliday_date(String holiday_date) {
		this.holiday_date = holiday_date;
	}
	public String getDay_of_week() {
		return day_of_week;
	}
	public void setDay_of_week(String day_of_week) {
		this.day_of_week = day_of_week;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
