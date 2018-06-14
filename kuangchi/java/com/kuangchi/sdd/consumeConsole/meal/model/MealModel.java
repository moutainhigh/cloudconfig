package com.kuangchi.sdd.consumeConsole.meal.model;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-8-18 下午6:07:18
 * @功能描述: 餐次维护模块-model
 */
public class MealModel {

	private Integer id;
	private String meal_num; //餐次编号
	private String meal_name; //餐次名称
	private String start_date; //开始时间
	private String end_date; //结束时间
	private Double amount_limit; //时段金额
	private String remark; //备注
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMeal_num() {
		return meal_num;
	}
	public void setMeal_num(String meal_num) {
		this.meal_num = meal_num;
	}
	public String getMeal_name() {
		return meal_name;
	}
	public void setMeal_name(String meal_name) {
		this.meal_name = meal_name;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public Double getAmount_limit() {
		return amount_limit;
	}
	public void setAmount_limit(Double amount_limit) {
		this.amount_limit = amount_limit;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
