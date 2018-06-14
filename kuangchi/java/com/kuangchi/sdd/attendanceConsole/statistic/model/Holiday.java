package com.kuangchi.sdd.attendanceConsole.statistic.model;

public class Holiday {
	  Integer holidayId;
	  String holidayNum;//'节假日编号',
	 String  holidayTypeNum;//'类型编号',
	 String holidayBeginDate;//'假期开始时间',
	 String holidayEndDate;//'假期结束时间',
	 String holidayScope;//'作用范围（全部|男|女）',
	 String holidayName;
	 Integer time_period;  //1 上午  2 下午  3 全天
	public Integer getHolidayId() {
		return holidayId;
	}
	public void setHolidayId(Integer holidayId) {
		this.holidayId = holidayId;
	}
	public String getHolidayNum() {
		return holidayNum;
	}
	public void setHolidayNum(String holidayNum) {
		this.holidayNum = holidayNum;
	}
	public String getHolidayTypeNum() {
		return holidayTypeNum;
	}
	public void setHolidayTypeNum(String holidayTypeNum) {
		this.holidayTypeNum = holidayTypeNum;
	}
	public String getHolidayBeginDate() {
		return holidayBeginDate;
	}
	public void setHolidayBeginDate(String holidayBeginDate) {
		this.holidayBeginDate = holidayBeginDate;
	}
	public String getHolidayEndDate() {
		return holidayEndDate;
	}
	public void setHolidayEndDate(String holidayEndDate) {
		this.holidayEndDate = holidayEndDate;
	}
	public String getHolidayScope() {
		return holidayScope;
	}
	public void setHolidayScope(String holidayScope) {
		this.holidayScope = holidayScope;
	}
	public String getHolidayName() {
		return holidayName;
	}
	public void setHolidayName(String holidayName) {
		this.holidayName = holidayName;
	}
	public Integer getTime_period() {
		return time_period;
	}
	public void setTime_period(Integer time_period) {
		this.time_period = time_period;
	}
	
	 
	 
}
