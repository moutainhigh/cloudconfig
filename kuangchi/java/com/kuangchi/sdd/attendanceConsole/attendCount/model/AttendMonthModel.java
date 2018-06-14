package com.kuangchi.sdd.attendanceConsole.attendCount.model;

import java.math.BigDecimal;


public class AttendMonthModel {
	private String Id;
	private String staffNum;
	private String staffNo;
	private String staffName;
	private String sex;//0男
	private String everyMonth;//======
	
	private String sjbm_mc; //上级部门名称
	private String deptName;//部门名称
	private String deptNum;
	public String getDeptNum() {
		return deptNum;
	}
	public void setDeptNum(String deptNum) {
		this.deptNum = deptNum;
	}
	private BigDecimal workDaysTotal;//本月需要工作天数
	private BigDecimal workDay;//实际上班天数
	private BigDecimal workDayNormal;//正班上班天数
	private BigDecimal overDay;//加班天数
	
	private BigDecimal workTimeNormal;//正常上班时间(单位小时)
	private BigDecimal overWorkTimeNormal;//工作日加班时间(单位小时)
	
	private BigDecimal overWorkTimeWeekend;//周末加班时间(单位小时)
	private BigDecimal overWorkTimeHoliday;//节假日加班时间(单位小时)
	private BigDecimal workTimeAll;//所有工作时间(单位小时)
	private BigDecimal workTimeAvg;//出勤日平均值(workTime_all/出勤天数)(单位小时)
	private BigDecimal outTotalTime;//外出总时间(单位小时)
	
	private BigDecimal leaveTime;//请假时间(单位小时)
	private Integer inKg;//旷工总次数
	private Integer inZtcd;//迟到早退次数
	private Integer cardNot;//缺打卡次数
	private Integer cardStatus;//补打卡次数
	
	
	public BigDecimal getWorkDayNormal() {
		return workDayNormal;
	}
	public void setWorkDayNormal(BigDecimal workDayNormal) {
		this.workDayNormal = workDayNormal;
	}
	public BigDecimal getOverDay() {
		return overDay;
	}
	public void setOverDay(BigDecimal overDay) {
		this.overDay = overDay;
	}
	public String getSjbm_mc() {
		return sjbm_mc;
	}
	public void setSjbm_mc(String sjbm_mc) {
		this.sjbm_mc = sjbm_mc;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getStaffNo() {
		return staffNo;
	}
	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}
	public String getStaffNum() {
		return staffNum;
	}
	public void setStaffNum(String staffNum) {
		this.staffNum = staffNum;
	}
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getEveryMonth() {
		return everyMonth;
	}
	public void setEveryMonth(String everyMonth) {
		this.everyMonth = everyMonth;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public BigDecimal getWorkDaysTotal() {
		return workDaysTotal;
	}
	public void setWorkDaysTotal(BigDecimal workDaysTotal) {
		this.workDaysTotal = workDaysTotal;
	}
	public BigDecimal getWorkDay() {
		return workDay;
	}
	public void setWorkDay(BigDecimal workDay) {
		this.workDay = workDay;
	}
	public BigDecimal getWorkTimeNormal() {
		return workTimeNormal;
	}
	public void setWorkTimeNormal(BigDecimal workTimeNormal) {
		this.workTimeNormal = workTimeNormal;
	}
	public BigDecimal getOverWorkTimeNormal() {
		return overWorkTimeNormal;
	}
	public void setOverWorkTimeNormal(BigDecimal overWorkTimeNormal) {
		this.overWorkTimeNormal = overWorkTimeNormal;
	}
	public BigDecimal getOverWorkTimeWeekend() {
		return overWorkTimeWeekend;
	}
	public void setOverWorkTimeWeekend(BigDecimal overWorkTimeWeekend) {
		this.overWorkTimeWeekend = overWorkTimeWeekend;
	}
	public BigDecimal getOverWorkTimeHoliday() {
		return overWorkTimeHoliday;
	}
	public void setOverWorkTimeHoliday(BigDecimal overWorkTimeHoliday) {
		this.overWorkTimeHoliday = overWorkTimeHoliday;
	}
	public BigDecimal getWorkTimeAll() {
		return workTimeAll;
	}
	public void setWorkTimeAll(BigDecimal workTimeAll) {
		this.workTimeAll = workTimeAll;
	}
	public BigDecimal getWorkTimeAvg() {
		return workTimeAvg;
	}
	public void setWorkTimeAvg(BigDecimal workTimeAvg) {
		this.workTimeAvg = workTimeAvg;
	}
	public BigDecimal getOutTotalTime() {
		return outTotalTime;
	}
	public void setOutTotalTime(BigDecimal outTotalTime) {
		this.outTotalTime = outTotalTime;
	}
	public BigDecimal getLeaveTime() {
		return leaveTime;
	}
	public void setLeaveTime(BigDecimal leaveTime) {
		this.leaveTime = leaveTime;
	}
	public Integer getInKg() {
		return inKg;
	}
	public void setInKg(Integer inKg) {
		this.inKg = inKg;
	}
	public Integer getInZtcd() {
		return inZtcd;
	}
	public void setInZtcd(Integer inZtcd) {
		this.inZtcd = inZtcd;
	}
	public Integer getCardNot() {
		return cardNot;
	}
	public void setCardNot(Integer cardNot) {
		this.cardNot = cardNot;
	}
	public Integer getCardStatus() {
		return cardStatus;
	}
	public void setCardStatus(Integer cardStatus) {
		this.cardStatus = cardStatus;
	}
	
	
}
