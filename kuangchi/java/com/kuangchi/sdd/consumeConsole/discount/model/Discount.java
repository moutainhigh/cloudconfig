package com.kuangchi.sdd.consumeConsole.discount.model;



public class Discount {
	private Integer id;
	private Double discount_rate;//折扣率
	private String discount_num;//折扣代码
	private String discount_name;//折扣名称
	private String start_date;//开始时间
	private String end_date;//结束时间
	private Integer delete_flag;//删除标志
	private String create_time;//创建时间
	private String description;//描述
	private Integer discount_state;//折扣期状态
	private String create_user;//创建者

	private Integer discount_rates;
	
	
	
	public String getDiscount_name() {
		return discount_name;
	}
	public void setDiscount_name(String discount_name) {
		this.discount_name = discount_name;
	}
	public Integer getDiscount_rates() {
		return discount_rates;
	}
	public void setDiscount_rates(Integer discount_rates) {
		this.discount_rates = discount_rates;
	}
	public Integer getDiscount_state() {
		return discount_state;
	}
	public void setDiscount_state(Integer discount_state) {
		this.discount_state = discount_state;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getDiscount_rate() {
		return discount_rate;
	}
	public void setDiscount_rate(Double discount_rate) {
		this.discount_rate = discount_rate;
	}
	
	public String getDiscount_num() {
		return discount_num;
	}
	public void setDiscount_num(String discount_num) {
		this.discount_num = discount_num;
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
	public Integer getDelete_flag() {
		return delete_flag;
	}
	public void setDelete_flag(Integer delete_flag) {
		this.delete_flag = delete_flag;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreate_user() {
		return create_user;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
	
	
}
