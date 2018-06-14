package com.kuangchi.sdd.attendanceConsole.attendRecord.model;

public class AttendRecordModel {

	String staff_no ;// '用户工号',
	String staff_num ;// '用户编号',
	String staff_name;// '用户名',
	String every_date;//'每天日期',
	String work_begin_time1;// '上午上班打卡开始时间',
	String work_end_time1;// '上午下班打卡结束时间',
	String work_begin_time2;// '下午上班打卡开始时间',
	String work_end_time2;// '下午下班打卡结束时间',
	Double leave_total_time=0.0;// '本日请假总共时间',
	Double out_total_time=0.0;//本日外出时间
	Double should_work_time=0.0;
  	Double work_time=0.0;// '正常工作时间',
  	String remark;// '备注，迟到早退等',
  	String today_work;//'是否上班，1上班，0放假',
  	Double later_time=0.0;// '迟到时间',
  	Double early_time=0.0;// '早退时间',
  	Double kg_time=0.0;// '旷工时间',
  	Double over_time=0.0;// '加班时间',
  	Integer isholiday=0;// '是否放假',
  	Integer isvocation=0;
  	Integer isException = 0; //是否异常
	   
  	
	public String getStaff_no() {
		return staff_no;
	}
	public void setStaff_no(String staff_no) {
		this.staff_no = staff_no;
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
	public String getEvery_date() {
		return every_date;
	}
	public void setEvery_date(String every_date) {
		this.every_date = every_date;
	}
	public String getWork_begin_time1() {
		return work_begin_time1;
	}
	public void setWork_begin_time1(String work_begin_time1) {
		this.work_begin_time1 = work_begin_time1;
	}
	public String getWork_end_time1() {
		return work_end_time1;
	}
	public void setWork_end_time1(String work_end_time1) {
		this.work_end_time1 = work_end_time1;
	}
	public String getWork_begin_time2() {
		return work_begin_time2;
	}
	public void setWork_begin_time2(String work_begin_time2) {
		this.work_begin_time2 = work_begin_time2;
	}
	public String getWork_end_time2() {
		return work_end_time2;
	}
	public void setWork_end_time2(String work_end_time2) {
		this.work_end_time2 = work_end_time2;
	}
	public Double getLeave_total_time() {
		return leave_total_time;
	}
	public void setLeave_total_time(Double leave_total_time) {
		this.leave_total_time = leave_total_time;
	}
	public Double getOut_total_time() {
		return out_total_time;
	}
	public void setOut_total_time(Double out_total_time) {
		this.out_total_time = out_total_time;
	}
	public Double getShould_work_time() {
		return should_work_time;
	}
	public void setShould_work_time(Double should_work_time) {
		this.should_work_time = should_work_time;
	}
	public Double getWork_time() {
		return work_time;
	}
	public void setWork_time(Double work_time) {
		this.work_time = work_time;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getToday_work() {
		return today_work;
	}
	public void setToday_work(String today_work) {
		this.today_work = today_work;
	}
	public Double getLater_time() {
		return later_time;
	}
	public void setLater_time(Double later_time) {
		this.later_time = later_time;
	}
	public Double getEarly_time() {
		return early_time;
	}
	public void setEarly_time(Double early_time) {
		this.early_time = early_time;
	}
	public Double getKg_time() {
		return kg_time;
	}
	public void setKg_time(Double kg_time) {
		this.kg_time = kg_time;
	}
	public Double getOver_time() {
		return over_time;
	}
	public void setOver_time(Double over_time) {
		this.over_time = over_time;
	}
	public Integer getIsholiday() {
		return isholiday;
	}
	public void setIsholiday(Integer isholiday) {
		this.isholiday = isholiday;
	}
	public Integer getIsvocation() {
		return isvocation;
	}
	public void setIsvocation(Integer isvocation) {
		this.isvocation = isvocation;
	}
	public Integer getIsException() {
		return isException;
	}
	public void setIsException(Integer isException) {
		this.isException = isException;
	}
}
