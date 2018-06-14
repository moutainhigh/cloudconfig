package com.kuangchi.sdd.consumeConsole.deptConsumeReport.model;

public class DeptConsumeReportModel {
	
	private  Integer   id;           //部门消费报表ID
	private  String   dept_name;     //部门名称
	private  String   dept_no;      //部门编号
	private  String   dept_num;      //部门代码
	private  String   vendor_name;     //商户名称
	private  String   vendor_num;      //商户编号
	private  String   meal1_amount;  //餐次一次数
	private  String   meal1_money;   //餐次一金额
	private  String   meal2_amount;  //餐次二次数
	private  String   meal2_money;   //餐次二金额
	private  String   meal3_amount;  //餐次三次数
	private  String   meal3_money;   //餐次三金额
	private  String   meal4_amount;  //餐次四次数
	private  String   meal4_money;   //餐次四金额
	private  String   meal5_amount;  //餐次五次数
	private  String   meal5_money;   //餐次五金额
	private  String   amount;        //合计次数
	private  String   money;         //合计金额
	private  String   every_date;    //统计日期
	private  String   begin_time;    //查询时间段开始时间
	private  String   end_time;      //查询时间段结束时间
	private  String   after_discount_money; //折后金额
	private  String   refund_total;  //退款总额
	private  String   cancel_total;  //撤销消费总额
	
	
	
	public String getRefund_total() {
		return refund_total;
	}
	public void setRefund_total(String refund_total) {
		this.refund_total = refund_total;
	}
	public String getCancel_total() {
		return cancel_total;
	}
	public void setCancel_total(String cancel_total) {
		this.cancel_total = cancel_total;
	}
	public String getAfter_discount_money() {
		return after_discount_money;
	}
	public void setAfter_discount_money(String after_discount_money) {
		this.after_discount_money = after_discount_money;
	}
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
	public String getMeal1_amount() {
		return meal1_amount;
	}
	public void setMeal1_amount(String meal1_amount) {
		this.meal1_amount = meal1_amount;
	}
	public String getMeal1_money() {
		return meal1_money;
	}
	public void setMeal1_money(String meal1_money) {
		this.meal1_money = meal1_money;
	}
	public String getMeal2_amount() {
		return meal2_amount;
	}
	public void setMeal2_amount(String meal2_amount) {
		this.meal2_amount = meal2_amount;
	}
	public String getMeal2_money() {
		return meal2_money;
	}
	public void setMeal2_money(String meal2_money) {
		this.meal2_money = meal2_money;
	}
	public String getMeal3_amount() {
		return meal3_amount;
	}
	public void setMeal3_amount(String meal3_amount) {
		this.meal3_amount = meal3_amount;
	}
	public String getMeal3_money() {
		return meal3_money;
	}
	public void setMeal3_money(String meal3_money) {
		this.meal3_money = meal3_money;
	}
	public String getMeal4_amount() {
		return meal4_amount;
	}
	public void setMeal4_amount(String meal4_amount) {
		this.meal4_amount = meal4_amount;
	}
	public String getMeal4_money() {
		return meal4_money;
	}
	public void setMeal4_money(String meal4_money) {
		this.meal4_money = meal4_money;
	}
	public String getMeal5_amount() {
		return meal5_amount;
	}
	public void setMeal5_amount(String meal5_amount) {
		this.meal5_amount = meal5_amount;
	}
	public String getMeal5_money() {
		return meal5_money;
	}
	public void setMeal5_money(String meal5_money) {
		this.meal5_money = meal5_money;
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
	public String getVendor_name() {
		return vendor_name;
	}
	public void setVendor_name(String vendor_name) {
		this.vendor_name = vendor_name;
	}
	public String getVendor_num() {
		return vendor_num;
	}
	public void setVendor_num(String vendor_num) {
		this.vendor_num = vendor_num;
	}
	public String getDept_no() {
		return dept_no;
	}
	public void setDept_no(String dept_no) {
		this.dept_no = dept_no;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	

}
