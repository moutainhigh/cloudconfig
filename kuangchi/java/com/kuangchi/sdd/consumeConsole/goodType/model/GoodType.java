package com.kuangchi.sdd.consumeConsole.goodType.model;

public class GoodType {
	private String id;
	private String type_num;//类别编码
	private String type_name;//类别名称
	private String price;//类别单价
	private String discount_name;//折扣名称
	private String discount_num;//折扣代码
	private String delete_flag;//删除标志
	private String end_date;//折扣结束时间
	private String discount_state;//折扣状态
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType_num() {
		return type_num;
	}
	public void setType_num(String type_num) {
		this.type_num = type_num;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getDiscount_name() {
		return discount_name;
	}
	public void setDiscount_name(String discount_name) {
		this.discount_name = discount_name;
	}
	public String getDiscount_num() {
		return discount_num;
	}
	public void setDiscount_num(String discount_num) {
		this.discount_num = discount_num;
	}
	public String getDelete_flag() {
		return delete_flag;
	}
	public void setDelete_flag(String delete_flag) {
		this.delete_flag = delete_flag;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getDiscount_state() {
		return discount_state;
	}
	public void setDiscount_state(String discount_state) {
		this.discount_state = discount_state;
	}
	
}
