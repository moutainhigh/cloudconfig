package com.kuangchi.sdd.consumeConsole.good.model;

public class Discount {
	private String discount_rate;//折扣率
	private String discount_num;//折扣代码
	private String discount_name;//折扣名称
	private String start_date;//有效期开始时间
	private String end_date;//有效期结束时间
	public String getDiscount_rate() {
		return discount_rate;
	}
	public void setDiscount_rate(String discount_rate) {
		this.discount_rate = discount_rate;
	}
	public String getDiscount_num() {
		return discount_num;
	}
	public void setDiscount_num(String discount_num) {
		this.discount_num = discount_num;
	}
	public String getDiscount_name() {
		return discount_name;
	}
	public void setDiscount_name(String discount_name) {
		this.discount_name = discount_name;
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
	
}
