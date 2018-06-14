package com.kuangchi.sdd.attendanceConsole.attendCount.model;

public class AttendDetailInfoModel {
	//date 表中的字段
	private Integer Id;//用户编号
	private Integer dutyId;//班次
	private String staffNum;//用户名
	private String staffNo;//用户名
	private String staffName;
	private String dutyName;//班次
	
	private String sex;//性别0 男
	private String everyDate;//每天日期
	
	private String deptName;//部门名称------------
	
	private String morningworkBeginTime;//早上上班打卡时间
	private String morningworkEndTime;//早上下班打卡时间
	private String afternoonworkBeginTime;//下午上班打卡时间
	private String afternoonworkEndTime;//下午下班打卡时间
	
	
	private Double leaveTotalTime;//请假总共时间
	private Double outTotalTime;//外出总共时间
	
	private Integer inWork;//是否离职0是
	private Double workTime;//正常工作时间
	private String remark;//备注，迟到或者早退等
	private String todayWork;//是否上班，1上班，0放假
	
	private Double laterTime;//迟到时间
	private Double earlyTime;//早退时间
	private Double kgTime;//旷工时间
	private Integer cardNot;//缺打卡次数
	private Integer cardStatus;//补打卡次数
	
	private Double overTime;//加班时间
	private Integer isHoliday;//是否放假 0是
	
	
	private Integer kgCount;//旷工次数
	private Integer earlyCount;//早退次数
	private Integer laterCount;//迟到次数
	
	private String laterTimeStr;
	private String earlyTimeStr;
	private String kgTimeStr;
	private String workTimeStr;
	private String overTimeStr;
	private String kgType; //旷工类型  0 无类型， 1 上午，2 下午 3 全天
	
	private Integer isExtralwork; //是否补班
	
	private String shouldWorkTimeStr;
	
	
	
	public String getShouldWorkTimeStr() {
		return shouldWorkTimeStr;
	}
	public void setShouldWorkTimeStr(String shouldWorkTimeStr) {
		this.shouldWorkTimeStr = shouldWorkTimeStr;
	}
	public Integer getIsExtralwork() {
		return isExtralwork;
	}
	public void setIsExtralwork(Integer isExtralwork) {
		this.isExtralwork = isExtralwork;
	}
	public String getKgType() {
		return kgType;
	}
	public void setKgType(String kgType) {
		this.kgType = kgType;
	}
	public String getStaffNo() {
		return staffNo;
	}
	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}
	public String getOverTimeStr() {
		return overTimeStr;
	}
	public void setOverTimeStr(String overTimeStr) {
		this.overTimeStr = overTimeStr;
	}
	public String getKgTimeStr() {
		return kgTimeStr;
	}
	public void setKgTimeStr(String kgTimeStr) {
		this.kgTimeStr = kgTimeStr;
	}
	public String getWorkTimeStr() {
		return workTimeStr;
	}
	public void setWorkTimeStr(String workTimeStr) {
		this.workTimeStr = workTimeStr;
	}


	private String leaveTotalTimeStr;//格式：**天**时**分**秒
	private String outTotalTimeStr;//格式：**天**时**分**秒
	
	
	 
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


	//请假表中的字段
	private String leaveDetail;//2016-04-05 14:54 至 2016-04-19 14:54
	private String leaveCatetoryName;//请假类型
	private String leaveReason;//请假原因 


	//外出表中的字段
	private String  outDetail;//2016-04-05 14:54 至 2016-04-19 14:54
	private Double shouldWorkTime;//每日应该上班时间
	private Integer isVocation;//是否是 公休日 0 否 1 是
	
	
	private Boolean isMidCheck;//中间是否考勤
	private Integer noCheckSet; // 免打卡设置   1,2,4,8 分别表示上午上班，上午下班，下午上班，下午下班 ；弹性工作班 1表示上班打卡 8 表示下班打卡；其中的组合进行或运算，如上午上班和上午下班都不用打卡则该值应为1|3=3
	
	
	
	public Integer getNoCheckSet() {
		return noCheckSet;
	}
	public void setNoCheckSet(Integer noCheckSet) {
		this.noCheckSet = noCheckSet;
	}
	public Boolean getIsMidCheck() {
		return isMidCheck;
	}
	public void setIsMidCheck(Boolean isMidCheck) {
		this.isMidCheck = isMidCheck;
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
	public Integer getInWork() {
		return inWork;
	}
	public void setInWork(Integer inWork) {
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
	public Integer getIsHoliday() {
		return isHoliday;
	}
	public void setIsHoliday(Integer isHoliday) {
		this.isHoliday = isHoliday;
	}
	public String getLeaveDetail() {
		return leaveDetail;
	}
	public void setLeaveDetail(String leaveDetail) {
		this.leaveDetail = leaveDetail;
	}
	public String getLeaveCatetoryName() {
		return leaveCatetoryName;
	}
	public void setLeaveCatetoryName(String leaveCatetoryName) {
		this.leaveCatetoryName = leaveCatetoryName;
	}
	public String getLeaveReason() {
		return leaveReason;
	}
	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}
	public String getOutDetail() {
		return outDetail;
	}
	public void setOutDetail(String outDetail) {
		this.outDetail = outDetail;
	}
	public Double getShouldWorkTime() {
		return shouldWorkTime;
	}
	public void setShouldWorkTime(Double shouldWorkTime) {
		this.shouldWorkTime = shouldWorkTime;
	}
	public Integer getIsVocation() {
		return isVocation;
	}
	public void setIsVocation(Integer isVocation) {
		this.isVocation = isVocation;
	}
	

}
