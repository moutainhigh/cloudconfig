package com.kuangchi.sdd.consumeConsole.consumeRecord.model;

import java.math.BigDecimal;

public class ConsumeRecord {
	
	private Integer id;//记录流水号
	private String record_flow_num;//消费记录流水号
	private String account_num;//账号
	private String consume_time; //消费时间
	private String create_time;//创建时间
	private String meal_num;//餐次代码
	private String meal_name;//餐次名称
	private String meal_start_time;//餐次开始时间
	private String meal_end_time;//餐次结束时间
	private String card_num;//卡号
	private String staff_num;//员工编号
	private String staff_name;//员工姓名
	private String dept_num;//部门代码
	private String staff_no;//员工工号
	private String staff_img;//员工头像
	private String dept_no;//部门编号
	private String dept_name;//部门名称
	private String device_num;//设备编号
	private String device_name;//设备名称
	private String group_num;//组号
	private String group_name;//组号名称
	private BigDecimal inbound;//收入金额
	private BigDecimal outbound;//支出金额
	private BigDecimal balance;//余额
	private String count_flag;//账户标志
	private String card_flow_no;//卡流水号 由消费机上报
	private String record_flow_no;//记录流水号 由消费机上报
	private String remark;//备注
	private Integer delete_flag;//删除标志 
	private String type; //消费类型   1 消费   2 撤销消费
	private String refund; //退款状态   0 未退款	 1 已退款
	private String isCancel; //撤销状态  0 未撤销   1已撤销
	private String good_num; //商品编号
	private String good_name; //商品名称
	private BigDecimal good_price; //商品价格
	private String good_type_num; //商品类型编号
	private String good_type_name; //商品类型名称
	private BigDecimal good_type_price; //商品类型价格
	private String vendor_num;//商户编号
	private String vendor_name;//商户名称
	private String vendor_dealer_name;//商家名称
	
	
	
	
	
	
	public String getGood_type_num() {
		return good_type_num;
	}
	public void setGood_type_num(String good_type_num) {
		this.good_type_num = good_type_num;
	}
	public String getGood_type_name() {
		return good_type_name;
	}
	public void setGood_type_name(String good_type_name) {
		this.good_type_name = good_type_name;
	}
	public BigDecimal getGood_type_price() {
		return good_type_price;
	}
	public void setGood_type_price(BigDecimal good_type_price) {
		this.good_type_price = good_type_price;
	}
	
	public String getVendor_num() {
		return vendor_num;
	}
	public void setVendor_num(String vendor_num) {
		this.vendor_num = vendor_num;
	}
	public String getVendor_name() {
		return vendor_name;
	}
	public void setVendor_name(String vendor_name) {
		this.vendor_name = vendor_name;
	}
	public String getVendor_dealer_name() {
		return vendor_dealer_name;
	}
	public void setVendor_dealer_name(String vendor_dealer_name) {
		this.vendor_dealer_name = vendor_dealer_name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRecord_flow_num() {
		return record_flow_num;
	}
	public void setRecord_flow_num(String record_flow_num) {
		this.record_flow_num = record_flow_num;
	}
	public String getAccount_num() {
		return account_num;
	}
	public void setAccount_num(String account_num) {
		this.account_num = account_num;
	}
	public String getConsume_time() {
		return consume_time;
	}
	public void setConsume_time(String consume_time) {
		this.consume_time = consume_time;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
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
	public String getMeal_start_time() {
		return meal_start_time;
	}
	public void setMeal_start_time(String meal_start_time) {
		this.meal_start_time = meal_start_time;
	}
	public String getMeal_end_time() {
		return meal_end_time;
	}
	public void setMeal_end_time(String meal_end_time) {
		this.meal_end_time = meal_end_time;
	}
	public String getCard_num() {
		return card_num;
	}
	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}
	public String getStaff_num() {
		return staff_num;
	}
	public void setStaff_num(String staff_num) {
		this.staff_num = staff_num;
	}
	public String getStaff_name() {
		return staff_name;
	}
	public void setStaff_name(String staff_name) {
		this.staff_name = staff_name;
	}
	public String getStaff_img() {
		return staff_img;
	}
	public void setStaff_img(String staff_img) {
		this.staff_img = staff_img;
	}
	public String getDept_num() {
		return dept_num;
	}
	public void setDept_num(String dept_num) {
		this.dept_num = dept_num;
	}
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public String getDevice_num() {
		return device_num;
	}
	public void setDevice_num(String device_num) {
		this.device_num = device_num;
	}
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	public String getGroup_num() {
		return group_num;
	}
	public void setGroup_num(String group_num) {
		this.group_num = group_num;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public BigDecimal getInbound() {
		return inbound;
	}
	public void setInbound(BigDecimal inbound) {
		this.inbound = inbound;
	}
	public BigDecimal getOutbound() {
		return outbound;
	}
	public void setOutbound(BigDecimal outbound) {
		this.outbound = outbound;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getCount_flag() {
		return count_flag;
	}
	public void setCount_flag(String count_flag) {
		this.count_flag = count_flag;
	}
	public String getCard_flow_no() {
		return card_flow_no;
	}
	public void setCard_flow_no(String card_flow_no) {
		this.card_flow_no = card_flow_no;
	}
	public String getRecord_flow_no() {
		return record_flow_no;
	}
	public void setRecord_flow_no(String record_flow_no) {
		this.record_flow_no = record_flow_no;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getDelete_flag() {
		return delete_flag;
	}
	public void setDelete_flag(Integer delete_flag) {
		this.delete_flag = delete_flag;
	}
	public String getStaff_no() {
		return staff_no;
	}
	public void setStaff_no(String staff_no) {
		this.staff_no = staff_no;
	}
	public String getDept_no() {
		return dept_no;
	}
	public void setDept_no(String dept_no) {
		this.dept_no = dept_no;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getRefund() {
		return refund;
	}
	public void setRefund(String refund) {
		this.refund = refund;
	}
	public String getIsCancel() {
		return isCancel;
	}
	public void setIsCancel(String isCancel) {
		this.isCancel = isCancel;
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
	public BigDecimal getGood_price() {
		return good_price;
	}
	public void setGood_price(BigDecimal good_price) {
		this.good_price = good_price;
	}
	
	
	
}
