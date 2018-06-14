package com.kuangchi.sdd.consumeConsole.deptRechargeReport.model;

public class DeptRechargeReportModel {
	
	private  Integer   id;           //部门充值报表ID
	private  String   dept_name;     //部门名称
	private  String   dept_no;      //部门编号
	private  String   dept_num;      //部门代码
	private  String   total_amount;  //合计次数
	private  String   total_money;   //合计金额
	private  String   every_date;    //统计日期
	private  String   begin_time;    //查询时间段开始时间
	private  String   end_time;      //查询时间段结束时间
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public String getDept_num() {
		return dept_num;
	}
	public void setDept_num(String dept_num) {
		this.dept_num = dept_num;
	}
	public String getEvery_date() {
		return every_date;
	}
	public void setEvery_date(String every_date) {
		this.every_date = every_date;
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
	public String getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}
	public String getTotal_money() {
		return total_money;
	}
	public void setTotal_money(String total_money) {
		this.total_money = total_money;
	}
	public String getDept_no() {
		return dept_no;
	}
	public void setDept_no(String dept_no) {
		this.dept_no = dept_no;
	}
	
    
}
