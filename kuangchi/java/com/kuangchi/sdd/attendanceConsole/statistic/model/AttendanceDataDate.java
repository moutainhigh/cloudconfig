package com.kuangchi.sdd.attendanceConsole.statistic.model;

public class AttendanceDataDate {
	   Integer id;
	   Integer duty_id;
	   String staff_num ;// '用户编号',
	   String staff_name;// '用户名',
	   Integer sex;// '性别',
	   String every_date;//'每天日期',
	   String dept_num;// '部门编号',
	   String work_begin_time1;// '上午上班打卡开始时间',
	   String work_end_time1;// '上午下班打卡结束时间',
	   String work_begin_time2;// '下午上班打卡开始时间',
	   String work_end_time2;// '下午下班打卡结束时间',
	   Double leave_total_time=0.0;// '本日请假总共时间',
	   Double out_total_time=0.0;//本日外出时间
	   Integer in_work=1;// '是否离职，0是，1否',
	   Double should_work_time=0.0;
	   Double work_time=0.0;// '正常工作时间',
	   String remark;// '备注，迟到早退等',
	   String today_work;//'是否上班，1上班，0放假',
	   Double later_time=0.0;// '迟到时间',
	   Double early_time=0.0;// '早退时间',
	   Double kg_time=0.0;// '旷工时间',
	   Integer kg_count=0; //旷工次数
	   Integer early_count=0;//早退次数
	   Integer later_count=0;//迟到次数
	   Integer card_not=0;// '缺打卡次数',
	   Integer card_status=0;// '补打卡次数',
	   Double over_time=0.0;// '加班时间',
	   Integer isholiday=0;// '是否放假',
	   Integer isvocation=0;//是否为节假日
	   Integer no_check_set=0;//是否免打卡
	   Integer kg_type=0;//旷工类型  1 上午 2 下午  3 全天
	   String  dept_name ;//部门名称
	   String staff_no;//员工工号
	   Integer exception_flag=1;//异常类型  1默认为正常  0 异常
	   Integer is_extralwork=0;//是否是补班  0  否  1 是
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	
	
	
	public Integer getDuty_id() {
		return duty_id;
	}
	public void setDuty_id(Integer duty_id) {
		this.duty_id = duty_id;
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
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getEvery_date() {
		return every_date;
	}
	public void setEvery_date(String every_date) {
		this.every_date = every_date;
	}
	public String getDept_num() {
		return dept_num;
	}
	public void setDept_num(String dept_num) {
		this.dept_num = dept_num;
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
	public Integer getIn_work() {
		return in_work;
	}
	public void setIn_work(Integer in_work) {
		this.in_work = in_work;
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


	
	public Integer getCard_not() {
		return card_not;
	}
	public void setCard_not(Integer card_not) {
		this.card_not = card_not;
	}
	public Integer getCard_status() {
		return card_status;
	}
	public void setCard_status(Integer card_status) {
		this.card_status = card_status;
	}

	public Integer getIsholiday() {
		return isholiday;
	}
	public void setIsholiday(Integer isholiday) {
		this.isholiday = isholiday;
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
	public Double getShould_work_time() {
		return should_work_time;
	}
	public void setShould_work_time(Double should_work_time) {
		this.should_work_time = should_work_time;
	}
	public Integer getIsvocation() {
		return isvocation;
	}
	public void setIsvocation(Integer isvocation) {
		this.isvocation = isvocation;
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
	public Integer getKg_count() {
		return kg_count;
	}
	public void setKg_count(Integer kg_count) {
		this.kg_count = kg_count;
	}
	public Integer getEarly_count() {
		return early_count;
	}
	public void setEarly_count(Integer early_count) {
		this.early_count = early_count;
	}
	public Integer getLater_count() {
		return later_count;
	}
	public void setLater_count(Integer later_count) {
		this.later_count = later_count;
	}
	public Integer getNo_check_set() {
		return no_check_set;
	}
	public void setNo_check_set(Integer no_check_set) {
		this.no_check_set = no_check_set;
	}
	public Integer getKg_type() {
		return kg_type;
	}
	public void setKg_type(Integer kg_type) {
		this.kg_type = kg_type;
	}
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public String getStaff_no() {
		return staff_no;
	}
	public void setStaff_no(String staff_no) {
		this.staff_no = staff_no;
	}
	public Integer getException_flag() {
		return exception_flag;
	}
	public void setException_flag(Integer exception_flag) {
		this.exception_flag = exception_flag;
	}
	public Integer getIs_extralwork() {
		return is_extralwork;
	}
	public void setIs_extralwork(Integer is_extralwork) {
		this.is_extralwork = is_extralwork;
	}
	
	   
	   
	   
	   
	   
}
