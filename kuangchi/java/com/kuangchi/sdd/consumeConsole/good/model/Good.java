package com.kuangchi.sdd.consumeConsole.good.model;

public class Good{
	private String id;
	private String good_num;//商品编号
	private String good_name;//商品名称
	private String vendor_num;//商户名称
	private String good_type_num;//商品类别编码
	private String price;//单价
	private String discount_num;//折扣代码
	private String discount_name;//折扣名称
	private String available_time;//商品有效期
	private String remark;//备注
	private String create_time;//创建时间
	private String delete_flag;//删除标志
	private String end_date;//折扣结束时间
	private String discount_state;//折扣状态
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGood_num() {
		return good_num;
	}
	public void setGood_num(String good_num) {
		this.good_num = good_num;
	}
	public String getGood_name() {
		return good_name;
	}
	public void setGood_name(String good_name) {
		this.good_name = good_name;
	}
	public String getVendor_num() {
		return vendor_num;
	}
	public void setVendor_num(String vendor_num) {
		this.vendor_num = vendor_num;
	}
	public String getGood_type_num() {
		return good_type_num;
	}
	public void setGood_type_num(String good_type_num) {
		this.good_type_num = good_type_num;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
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
	public String getAvailable_time() {
		return available_time;
	}
	public void setAvailable_time(String available_time) {
		this.available_time = available_time;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
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
