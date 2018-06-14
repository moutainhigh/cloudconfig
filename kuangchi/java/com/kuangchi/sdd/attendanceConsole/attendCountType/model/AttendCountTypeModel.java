package com.kuangchi.sdd.attendanceConsole.attendCountType.model;


public class AttendCountTypeModel {
	
	private String bm_dm;//部门编号
	private String bm_mc;//部门名称
	private String sjbm_mc;//部门名称
	private String staff_num;
	private String staff_name;
	private String staff_no;
	private String leave_catetory;	//请假类型值
	private String leaveType; 		//请假类型名称
	private String exception_type; 		//异常类型编号
	private String exception_type1; 		//迟到
	private String exception_type2; 		//早退
	private String exception_type4; 		//旷工
	private String exceptionTypeName; 		//异常名称
	private String from_time; 		//请假开始时间
	private String to_time;			//请假结束时间
	private String totalTime;		//总日期
	private String totalNumber;		//总次数
	private String begin_time;		//开始时间
	private String end_time;		//结束时间
	private String everyday_time;	//每天时间
	private String duty_time_type;	//1 上午上班 ，2 上午下班，3下午上班，4 下午下班
	private String time_interval;	//迟到时间或早退时间或旷工时间等 ，单位分钟
	private String leaveTotalNumber;	//请假总次数
	
	private Integer kg_count;  // 旷工次数
	private String kg_type; // 旷工类型
	
	private String layerDeptNum; // 分层部门代码	by yuman.gao
	
	
	public String getSjbm_mc() {
		return sjbm_mc;
	}
	public void setSjbm_mc(String sjbm_mc) {
		this.sjbm_mc = sjbm_mc;
	}
	public String getLeaveTotalNumber() {
		return leaveTotalNumber;
	}
	public void setLeaveTotalNumber(String leaveTotalNumber) {
		this.leaveTotalNumber = leaveTotalNumber;
	}
	public Integer getKg_count() {
		return kg_count;
	}
	public void setKg_count(Integer kg_count) {
		this.kg_count = kg_count;
	}
	public String getKg_type() {
		return kg_type;
	}
	public void setKg_type(String kg_type) {
		this.kg_type = kg_type;
	}
	public String getLayerDeptNum() {
		return layerDeptNum;
	}
	public void setLayerDeptNum(String layerDeptNum) {
		this.layerDeptNum = layerDeptNum;
	}
	public String getDuty_time_type() {
		return duty_time_type;
	}
	public void setDuty_time_type(String duty_time_type) {
		this.duty_time_type = duty_time_type;
	}
	public String getTime_interval() {
		return time_interval;
	}
	public void setTime_interval(String time_interval) {
		this.time_interval = time_interval;
	}
	public String getEveryday_time() {
		return everyday_time;
	}
	public void setEveryday_time(String everyday_time) {
		this.everyday_time = everyday_time;
	}
	public String getExceptionTypeName() {
		return exceptionTypeName;
	}
	public void setExceptionTypeName(String exceptionTypeName) {
		this.exceptionTypeName = exceptionTypeName;
	}
	public String getException_type1() {
		return exception_type1;
	}
	public void setException_type1(String exception_type1) {
		this.exception_type1 = exception_type1;
	}
	public String getException_type2() {
		return exception_type2;
	}
	public void setException_type2(String exception_type2) {
		this.exception_type2 = exception_type2;
	}
	public String getException_type4() {
		return exception_type4;
	}
	public void setException_type4(String exception_type4) {
		this.exception_type4 = exception_type4;
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
	public String getException_type() {
		return exception_type;
	}
	public void setException_type(String exception_type) {
		this.exception_type = exception_type;
	}
	public String getBm_dm() {
		return bm_dm;
	}
	public void setBm_dm(String bm_dm) {
		this.bm_dm = bm_dm;
	}
	public String getBm_mc() {
		return bm_mc;
	}
	public void setBm_mc(String bm_mc) {
		this.bm_mc = bm_mc;
	}
	public String getStaff_num() {
		return staff_num;
	}
	public void setStaff_num(String staff_num) {
		this.staff_num = staff_num;
	}
	public String getStaff_name() {
		return staff_name;
	}
	public void setStaff_name(String staff_name) {
		this.staff_name = staff_name;
	}
	public String getStaff_no() {
		return staff_no;
	}
	public void setStaff_no(String staff_no) {
		this.staff_no = staff_no;
	}
	public String getLeave_catetory() {
		return leave_catetory;
	}
	public void setLeave_catetory(String leave_catetory) {
		this.leave_catetory = leave_catetory;
	}
	public String getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}
	public String getFrom_time() {
		return from_time;
	}
	public void setFrom_time(String from_time) {
		this.from_time = from_time;
	}
	public String getTo_time() {
		return to_time;
	}
	public void setTo_time(String to_time) {
		this.to_time = to_time;
	}
	public String getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
	public String getTotalNumber() {
		return totalNumber;
	}
	public void setTotalNumber(String totalNumber) {
		this.totalNumber = totalNumber;
	}
	
	
	
}
