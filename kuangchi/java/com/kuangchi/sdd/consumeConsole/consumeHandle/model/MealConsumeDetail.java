package com.kuangchi.sdd.consumeConsole.consumeHandle.model;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-8-21 下午5:14:56
 * @功能描述: 餐次日消费流水-model
 */
public class MealConsumeDetail {
	
	private Integer id;
	private String every_date; //日期字符 如 2012-10-11 每天一条
	private String meal_num; //餐次编号
	private String meal_name; //餐次名称
	private Integer amount = 0; //商品总数
	private Double money = 0.0; //商品总金额
	private String create_time; //创建时间
	private Double cancel_total; // 撤销总额
	private Double refund_total; //退款总额
	private Double after_discount_money; //实际交易金额（折后金额）
	
	
	public Double getCancel_total() {
		return cancel_total;
	}
	public void setCancel_total(Double cancel_total) {
		this.cancel_total = cancel_total;
	}
	public Double getRefund_total() {
		return refund_total;
	}
	public void setRefund_total(Double refund_total) {
		this.refund_total = refund_total;
	}
	public Double getAfter_discount_money() {
		return after_discount_money;
	}
	public void setAfter_discount_money(Double after_discount_money) {
		this.after_discount_money = after_discount_money;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEvery_date() {
		return every_date;
	}
	public void setEvery_date(String every_date) {
		this.every_date = every_date;
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
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	
	
	
}
