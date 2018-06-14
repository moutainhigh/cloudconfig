package com.kuangchi.sdd.consumeConsole.goodType.model;

public class PriceHistoryModel {
	private int id;
	private String good_or_type_num;//商品编号或类别编号
	private String is_type;//类型
	private String old_price;//原价
	private String new_price;//现价
	private String create_time;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGood_or_type_num() {
		return good_or_type_num;
	}
	public void setGood_or_type_num(String good_or_type_num) {
		this.good_or_type_num = good_or_type_num;
	}
	public String getIs_type() {
		return is_type;
	}
	public void setIs_type(String is_type) {
		this.is_type = is_type;
	}
	public String getOld_price() {
		return old_price;
	}
	public void setOld_price(String old_price) {
		this.old_price = old_price;
	}
	public String getNew_price() {
		return new_price;
	}
	public void setNew_price(String new_price) {
		this.new_price = new_price;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	
}
