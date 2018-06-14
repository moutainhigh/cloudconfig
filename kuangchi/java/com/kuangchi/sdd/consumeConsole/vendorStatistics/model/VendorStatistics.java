package com.kuangchi.sdd.consumeConsole.vendorStatistics.model;

import java.math.BigDecimal;

public class VendorStatistics {
	
	private Integer id;//记录流水号
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
	private String BM_NO;//部门编号
	private String dept_name;//部门名称
	private String device_num;//设备编号
	private String device_name;//设备名称
	private String group_num;//组号
	private String group_name;//组号名称
	private double inbound;//收入金额
	private double outbound;//支出金额
	private double balance;//余额
	private String count_flag;//账户标志
	private String card_flow_no;//卡流水号 由消费机上报
	private String record_flow_no;//记录流水号 由消费机上报
	private String remark;//备注
	private Integer delete_flag;//删除标志 
	
	
	private String vendor_num;//商户编号
	private String vendor_name;//商户名称
	private String vendor_dealer_name;//商家姓名
	private String type;//消费类型 收支类型 0 充值 1 消费 2 撤销消费 3 退款
	
	private String begin_time;
	private String end_time;
	private String endTime;
	
	
	private double inbound_a;//充值收入金额
	private double inbound_b;//撤销消费金额
	private double outbound_a;//消费支出金额
	private double outbound_b;//退款支出金额
	
	private double inbound_money;//收入总金额
	private double outbound_money;//支出总金额
	
	
	
	
	
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public double getInbound_money() {
		return inbound_money;
	}
	public void setInbound_money(double inbound_money) {
		BigDecimal b=new BigDecimal(inbound_money);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.inbound_money = f1;
	}
	public double getOutbound_money() {
		return outbound_money;
	}
	public void setOutbound_money(double outbound_money) {
		BigDecimal b=new BigDecimal(outbound_money);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.outbound_money = f1;
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
	public String getVendor_dealer_name() {
		return vendor_dealer_name;
	}
	public void setVendor_dealer_name(String vendor_dealer_name) {
		this.vendor_dealer_name = vendor_dealer_name;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	
	public double getInbound() {
		return inbound;
	}
	public void setInbound(double inbound) {
		BigDecimal b=new BigDecimal(inbound);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();  
		this.inbound = f1;
	}
	public double getOutbound() {
		return outbound;
	}
	public void setOutbound(double outbound) {
		BigDecimal b=new BigDecimal(outbound);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.outbound = f1;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public double getInbound_a() {
		return inbound_a;
	}
	public void setInbound_a(double inbound_a) {
		BigDecimal b=new BigDecimal(inbound_a);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.inbound_a = f1;
	}
	public double getInbound_b() {
		return inbound_b;
	}
	public void setInbound_b(double inbound_b) {
		BigDecimal b=new BigDecimal(inbound_b);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.inbound_b = f1;
	}
	public double getOutbound_a() {
		return outbound_a;
	}
	public void setOutbound_a(double outbound_a) {
		BigDecimal b=new BigDecimal(outbound_a);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.outbound_a = f1;
	}
	public double getOutbound_b() {
		return outbound_b;
	}
	public void setOutbound_b(double outbound_b) {
		BigDecimal b=new BigDecimal(outbound_b);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.outbound_b = f1;
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
	public String getBM_NO() {
		return BM_NO;
	}
	public void setBM_NO(String bM_NO) {
		BM_NO = bM_NO;
	}
	
	
	
}
