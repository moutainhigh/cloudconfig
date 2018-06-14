package com.kuangchi.sdd.attendanceConsole.attendCount.model;

public class AttendDeptModel {
	private Integer Id;
	private String deptNum;
	private String deptNo;
	private String deptName;
	private String everyMonth;//月份
	private Double shouldWorkDays;//本月应该上满多少天,单位天
	private Double workDays;//实际出勤天数，单位天
	
	private Integer cardNot;//缺打卡次数
	private Double leaveTime;//请假时间，单位小时
	private Double outWorkTime;//外出总共时间，单位小时
	private Integer membersNum;//部门人数
	private Integer inZtcd;//迟到早退次数
	private Integer inKg;//旷工次数
	
	private Double allWorkTime; // 正常出勤时间合计（时）
	private Double avgWorkTime; //本月平均在岗时间（时）
	
	
	
	public Double getAllWorkTime() {
		return allWorkTime;
	}
	public void setAllWorkTime(Double allWorkTime) {
		this.allWorkTime = allWorkTime;
	}
	public Double getAvgWorkTime() {
		return avgWorkTime;
	}
	public void setAvgWorkTime(Double avgWorkTime) {
		this.avgWorkTime = avgWorkTime;
	}
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public String getDeptNum() {
		return deptNum;
	}
	public void setDeptNum(String deptNum) {
		this.deptNum = deptNum;
	}
	public String getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getEveryMonth() {
		return everyMonth;
	}
	public void setEveryMonth(String everyMonth) {
		this.everyMonth = everyMonth;
	}
	public Double getShouldWorkDays() {
		return shouldWorkDays;
	}
	public void setShouldWorkDays(Double shouldWorkDays) {
		this.shouldWorkDays = shouldWorkDays;
	}
	public Double getWorkDays() {
		return workDays;
	}
	public void setWorkDays(Double workDays) {
		this.workDays = workDays;
	}
	public Integer getCardNot() {
		return cardNot;
	}
	public void setCardNot(Integer cardNot) {
		this.cardNot = cardNot;
	}
	public Double getLeaveTime() {
		return leaveTime;
	}
	public void setLeaveTime(Double leaveTime) {
		this.leaveTime = leaveTime;
	}
	public Double getOutWorkTime() {
		return outWorkTime;
	}
	public void setOutWorkTime(Double outWorkTime) {
		this.outWorkTime = outWorkTime;
	}
	public Integer getMembersNum() {
		return membersNum;
	}
	public void setMembersNum(Integer membersNum) {
		this.membersNum = membersNum;
	}
	public Integer getInZtcd() {
		return inZtcd;
	}
	public void setInZtcd(Integer inZtcd) {
		this.inZtcd = inZtcd;
	}
	public Integer getInKg() {
		return inKg;
	}
	public void setInKg(Integer inKg) {
		this.inKg = inKg;
	}
}
