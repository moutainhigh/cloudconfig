package com.kuangchi.sdd.baseConsole.times.holidaytimes.model;


/**
 * @创建人　: 肖红丽
 * @创建时间: 2016-5-25下午1:55:59
 * @功能描述:节假日时段管理model
 * @参数描述:
 */
public class HolidayTimesModel {
	private Integer holidayTimes_id;//节假日ID
	private String holiday_time_num;	//节假日时段编号
	private String holiday_date; //节假日日期
	private String day_of_week;  //节假日星期设置
	private String create_user;	//创建人
	private String create_time; //创建时间
	private String description;	//描述
	private String validity_flag; //有效标志
	
	
	private String startDate;	//开始日期
	private String endDate;   //结束日期
	
	
	public String getHoliday_time_num() {
		return holiday_time_num;
	}
	public void setHoliday_time_num(String holiday_time_num) {
		this.holiday_time_num = holiday_time_num;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getValidity_flag() {
		return validity_flag;
	}
	public void setValidity_flag(String validity_flag) {
		this.validity_flag = validity_flag;
	}
	public Integer getHolidayTimes_id() {
		return holidayTimes_id;
	}
	public void setHolidayTimes_id(Integer holidayTimes_id) {
		this.holidayTimes_id = holidayTimes_id;
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
	public String getCreate_user() {
		return create_user;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
