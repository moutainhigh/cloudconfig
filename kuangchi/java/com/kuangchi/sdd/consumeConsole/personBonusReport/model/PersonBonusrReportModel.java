package com.kuangchi.sdd.consumeConsole.personBonusReport.model;

public class PersonBonusrReportModel {
       
	private  Integer  id;           //人员补助报表ID
	private  String   dept_name;     //部门名称
	private  String   dept_no;      //部门编号
	private  String   dept_num;      //部门代码
	private  String   staff_name;    //员工名称
	private  String   staff_no;     //员工编号
	private  String   staff_num;     //员工代码
	private  String   amount;        //统计合计次数   
	private  String   money;         //统计合计金额   
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
	public String getDept_no() {
		return dept_no;
	}
	public void setDept_no(String dept_no) {
		this.dept_no = dept_no;
	}
	public String getDept_num() {
		return dept_num;
	}
	public void setDept_num(String dept_num) {
		this.dept_num = dept_num;
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
	public String getStaff_num() {
		return staff_num;
	}
	public void setStaff_num(String staff_num) {
		this.staff_num = staff_num;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
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
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	
	
	
}
