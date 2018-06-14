package com.kuangchi.sdd.consumeConsole.consumeType.model;

public class ConsumeTypeModel {

	private Integer id; 
	private String consume_type_num; //类型代码
	private String consume_type_name; //类型名称
	private Integer limit_type; //限制类型   1 次数   2 额度   3 次数与额度同时限制
	private String meal_num;//餐次编号
	private Integer times_limit; //限制次数
	private String amount_limit; //限制额度
	private String create_time; //创建时间
	private Integer delete_flag; //删除标志    0 未删除     1 已删除
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getConsume_type_num() {
		return consume_type_num;
	}
	public void setConsume_type_num(String consume_type_num) {
		this.consume_type_num = consume_type_num;
	}
	public String getConsume_type_name() {
		return consume_type_name;
	}
	public void setConsume_type_name(String consume_type_name) {
		this.consume_type_name = consume_type_name;
	}
	public Integer getLimit_type() {
		return limit_type;
	}
	public void setLimit_type(Integer limit_type) {
		this.limit_type = limit_type;
	}
	public String getMeal_num() {
		return meal_num;
	}
	public void setMeal_num(String meal_num) {
		this.meal_num = meal_num;
	}
	public Integer getTimes_limit() {
		return times_limit;
	}
	public void setTimes_limit(Integer times_limit) {
		this.times_limit = times_limit;
	}
	public String getAmount_limit() {
		return amount_limit;
	}
	public void setAmount_limit(String amount_limit) {
		this.amount_limit = amount_limit;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public Integer getDelete_flag() {
		return delete_flag;
	}
	public void setDelete_flag(Integer delete_flag) {
		this.delete_flag = delete_flag;
	}
	
	
}
