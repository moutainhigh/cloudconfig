package com.kuangchi.sdd.attendanceConsole.attendCount.model;

public class AttendDateModel {
	private  Integer Id;//用户编号
	private Integer dutyId;//班次
	private String staffNum;//用户名
	private String staffNo;//用户名
	private String staffName;
	private String dutyName;//班次
	private String sjbm_mc; // 上级部门名称
	
	private String sex;//性别0 男
	private String everyDate;//每天日期
	private String deptName;//部门名称------------
	
	private String morningworkBeginTime;//早上上班打卡时间
	private String morningworkEndTime;//早上下班打卡时间
	private String afternoonworkBeginTime;//下午上班打卡时间
	private String afternoonworkEndTime;//下午下班打卡时间
	
	
	private Double leaveTotalTime;//请假总共时间
	private Double outTotalTime;//外出总共时间
	
	private String inWork;//是否离职0是
	private Double workTime;//正常工作时间
	private String remark;//备注，迟到或者早退等
	private String todayWork;//是否上班，1上班，0放假
	
	private Double laterTime;//迟到时间
	private Double earlyTime;//早退时间
	
	private String laterTimeStr;
	private String earlyTimeStr;
	
	 private String leaveTotalTimeStr;
	 private String outTotalTimeStr;
	
	
	
	private Double kgTime;//旷工时间
	private Integer cardNot;//缺打卡次数
	private Integer cardStatus;//补打卡次数
	
	private Integer kgCount;//旷工次数
	private Integer earlyCount;//早退次数
	private Integer laterCount;//迟到次数
	
	
	private Double overTime;//加班时间
	private String isHoliday;//是否是 节假日  0 否 1 是
	
	private String searchSDate;
	private String searchEDate;
	
	private Double shouldWorkTime;//每日应该上班时间
	private String isVocation;//是否是 公休日 0 否 1 是
	private String deptNum;//部门编号
	
	private Integer isException = 1; //是否异常   默认为否
	private String attendType; //考勤类型
	
	private Integer noCheckSet; // 免打卡设置   1,2,4,8 分别表示上午上班，上午下班，下午上班，下午下班 ；弹性工作班 1表示上班打卡 8 表示下班打卡；其中的组合进行或运算，如上午上班和上午下班都不用打卡则该值应为1|3=3
	private String noCheckSetStr; // 免打卡设置   1,2,4,8 分别表示上午上班，上午下班，下午上班，下午下班 ；弹性工作班 1表示上班打卡 8 表示下班打卡；其中的组合进行或运算，如上午上班和上午下班都不用打卡则该值应为1|3=3
	private String kgType; //旷工类型  0 无类型， 1 上午，2 下午 3 全天
	
	private String isExtralwork; // 是否补班  		by yuman.gao  2016-12-21
	
	private String leaveType; // 请假类型
	private String leaveTimePeriod; // 请假时段
	private String outTimePeriod; //外出时段
	
	
	public String getSjbm_mc() {
		return sjbm_mc;
	}
	public void setSjbm_mc(String sjbm_mc) {
		this.sjbm_mc = sjbm_mc;
	}
	public String getNoCheckSetStr() {
		return noCheckSetStr;
	}
	public void setNoCheckSetStr(String noCheckSetStr) {
		this.noCheckSetStr = noCheckSetStr;
	}
	public String getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}
	public String getLeaveTimePeriod() {
		return leaveTimePeriod;
	}
	public void setLeaveTimePeriod(String leaveTimePeriod) {
		this.leaveTimePeriod = leaveTimePeriod;
	}
	public String getOutTimePeriod() {
		return outTimePeriod;
	}
	public void setOutTimePeriod(String outTimePeriod) {
		this.outTimePeriod = outTimePeriod;
	}
	public String getIsExtralwork() {
		return isExtralwork;
	}
	public void setIsExtralwork(String isExtralwork) {
		this.isExtralwork = isExtralwork;
	}
	public String getKgType() {
		return kgType;
	}
	public void setKgType(String kgType) {
		this.kgType = kgType;
	}
	public Integer getNoCheckSet() {
		return noCheckSet;
	}
	public void setNoCheckSet(Integer noCheckSet) {
		this.noCheckSet = noCheckSet;
	}
	
	public String getAttendType() {
		return attendType;
	}
	public void setAttendType(String attendType) {
		this.attendType = attendType;
	}
	public Integer getIsException() {
		return isException;
	}
	public void setIsException(Integer isException) {
		this.isException = isException;
	}
	public String getStaffNo() {
		return staffNo;
	}
	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}
	public String getLeaveTotalTimeStr() {
		return leaveTotalTimeStr;
	}
	public void setLeaveTotalTimeStr(String leaveTotalTimeStr) {
		this.leaveTotalTimeStr = leaveTotalTimeStr;
	}
	public String getOutTotalTimeStr() {
		return outTotalTimeStr;
	}
	public void setOutTotalTimeStr(String outTotalTimeStr) {
		this.outTotalTimeStr = outTotalTimeStr;
	}
	public String getLaterTimeStr() {
		return laterTimeStr;
	}
	public void setLaterTimeStr(String laterTimeStr) {
		this.laterTimeStr = laterTimeStr;
	}
	public String getEarlyTimeStr() {
		return earlyTimeStr;
	}
	public void setEarlyTimeStr(String earlyTimeStr) {
		this.earlyTimeStr = earlyTimeStr;
	}
	public String getDeptNum() {
		return deptNum;
	}
	public void setDeptNum(String deptNum) {
		this.deptNum = deptNum;
	}
	public Integer getKgCount() {
		return kgCount;
	}
	public void setKgCount(Integer kgCount) {
		this.kgCount = kgCount;
	}
	public Integer getEarlyCount() {
		return earlyCount;
	}
	public void setEarlyCount(Integer earlyCount) {
		this.earlyCount = earlyCount;
	}
	public Integer getLaterCount() {
		return laterCount;
	}
	public void setLaterCount(Integer laterCount) {
		this.laterCount = laterCount;
	}
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public Integer getDutyId() {
		return dutyId;
	}
	public void setDutyId(Integer dutyId) {
		this.dutyId = dutyId;
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
	public String getDutyName() {
		return dutyName;
	}
	public void setDutyName(String dutyName) {
		this.dutyName = dutyName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getEveryDate() {
		return everyDate;
	}
	public void setEveryDate(String everyDate) {
		this.everyDate = everyDate;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getMorningworkBeginTime() {
		return morningworkBeginTime;
	}
	public void setMorningworkBeginTime(String morningworkBeginTime) {
		this.morningworkBeginTime = morningworkBeginTime;
	}
	public String getMorningworkEndTime() {
		return morningworkEndTime;
	}
	public void setMorningworkEndTime(String morningworkEndTime) {
		this.morningworkEndTime = morningworkEndTime;
	}
	public String getAfternoonworkBeginTime() {
		return afternoonworkBeginTime;
	}
	public void setAfternoonworkBeginTime(String afternoonworkBeginTime) {
		this.afternoonworkBeginTime = afternoonworkBeginTime;
	}
	public String getAfternoonworkEndTime() {
		return afternoonworkEndTime;
	}
	public void setAfternoonworkEndTime(String afternoonworkEndTime) {
		this.afternoonworkEndTime = afternoonworkEndTime;
	}
	public Double getLeaveTotalTime() {
		return leaveTotalTime;
	}
	public void setLeaveTotalTime(Double leaveTotalTime) {
		this.leaveTotalTime = leaveTotalTime;
	}
	public Double getOutTotalTime() {
		return outTotalTime;
	}
	public void setOutTotalTime(Double outTotalTime) {
		this.outTotalTime = outTotalTime;
	}
	
	public String getInWork() {
		return inWork;
	}
	public void setInWork(String inWork) {
		this.inWork = inWork;
	}
	public Double getWorkTime() {
		return workTime;
	}
	public void setWorkTime(Double workTime) {
		this.workTime = workTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTodayWork() {
		return todayWork;
	}
	public void setTodayWork(String todayWork) {
		this.todayWork = todayWork;
	}
	public Double getLaterTime() {
		return laterTime;
	}
	public void setLaterTime(Double laterTime) {
		this.laterTime = laterTime;
	}
	public Double getEarlyTime() {
		return earlyTime;
	}
	public void setEarlyTime(Double earlyTime) {
		this.earlyTime = earlyTime;
	}
	public Double getKgTime() {
		return kgTime;
	}
	public void setKgTime(Double kgTime) {
		this.kgTime = kgTime;
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
	public Double getOverTime() {
		return overTime;
	}
	public void setOverTime(Double overTime) {
		this.overTime = overTime;
	}
	
	public String getIsHoliday() {
		return isHoliday;
	}
	public void setIsHoliday(String isHoliday) {
		this.isHoliday = isHoliday;
	}
	public String getSearchSDate() {
		return searchSDate;
	}
	public void setSearchSDate(String searchSDate) {
		this.searchSDate = searchSDate;
	}
	public String getSearchEDate() {
		return searchEDate;
	}
	public void setSearchEDate(String searchEDate) {
		this.searchEDate = searchEDate;
	}
	public Double getShouldWorkTime() {
		return shouldWorkTime;
	}
	public void setShouldWorkTime(Double shouldWorkTime) {
		this.shouldWorkTime = shouldWorkTime;
	}
	public String getIsVocation() {
		return isVocation;
	}
	public void setIsVocation(String isVocation) {
		this.isVocation = isVocation;
	}
	
	
	
	

	
}
