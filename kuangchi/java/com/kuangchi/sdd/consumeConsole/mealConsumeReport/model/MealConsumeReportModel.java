package com.kuangchi.sdd.consumeConsole.mealConsumeReport.model;

public class MealConsumeReportModel {
	private  Integer  id;            //餐次消费报表ID
	private  String   meal_name;     //餐次名称
	private  String   meal_num;      //餐次编号
	private  String   amount;        //合计次数   
	private  String   money;         //折前总额 
	private  String   after_discount_money;         //折后总额   
	private  String   refund_total;         //退款总额 
	private  String   cancel_total;         //撤销消费总额   
	private  String   every_date;    //统计日期
	private  String   begin_time;    //查询时间段开始时间
	private  String   end_time;      //查询时间段结束时间
	
	
	public String getAfter_discount_money() {
		return after_discount_money;
	}
	public void setAfter_discount_money(String after_discount_money) {
		this.after_discount_money = after_discount_money;
	}
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
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMeal_name() {
		return meal_name;
	}
	public void setMeal_name(String meal_name) {
		this.meal_name = meal_name;
	}
	public String getMeal_num() {
		return meal_num;
	}
	public void setMeal_num(String meal_num) {
		this.meal_num = meal_num;
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
	
	
}
