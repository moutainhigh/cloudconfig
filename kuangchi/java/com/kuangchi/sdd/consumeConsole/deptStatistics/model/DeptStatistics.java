package com.kuangchi.sdd.consumeConsole.deptStatistics.model;

import java.math.BigDecimal;

public class DeptStatistics {
	
	private Integer id;
	private String detail_flow_no;//明细流水号
	private String account_num;//账号
	private String time;//时间
	private String staff_num;//员工编号
	private String staff_no;//员工工号
	private double inbound;//收入金额
	private double outbound;//支出金额
	private double balance;//余额
	private String staff_name;//员工姓名
	private String dept_no;//部门编号
	private String dept_num;//部门代码
	private String dept_name;//部门名称
	private String type;//收支类型 0 充值 1 消费 2 补助 3 补扣 4 撤销消费 5 退款 6 转入 7 转出
	private String deal_reason;//交易原因
	private String count_flag;//账户标志
	private String begin_time;//开始时间
	private String end_time;//结束时间
	private String account_type_name;//账户类型名称
	private String endTime;
	
	
	private double inbound_a;//充值总额
	private double inbound_b;//补助总额
	private double inbound_c;//撤销消费总额
	private double inbound_d;//转入总额
	private double outbound_a;//消费总额
	private double outbound_b;//补扣总额
	private double outbound_c;//退款总额
	private double outbound_d;//转出总额
	
	
	private int inbound_Num;//收入总次数
	private double inbound_Money;//收入总金额
	
	private int outbound_Num;//收入总次数
	private double outbound_Money;//收入总金额
	
	private int inbound_a_c;//充值总额次数
	private int inbound_b_c;//补助总额次数
	private int inbound_c_c;//撤销消费总额次数
	private int inbound_d_c;//转入总额次数
	private int outbound_a_c;//消费总额次数
	private int outbound_b_c;//补扣总额次数
	private int outbound_c_c;//退款总额次数
	private int outbound_d_c;//转出总额次数
	
	
	
	
	
	
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getAccount_type_name() {
		return account_type_name;
	}
	public void setAccount_type_name(String account_type_name) {
		this.account_type_name = account_type_name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDetail_flow_no() {
		return detail_flow_no;
	}
	public void setDetail_flow_no(String detail_flow_no) {
		this.detail_flow_no = detail_flow_no;
	}
	public String getAccount_num() {
		return account_num;
	}
	public void setAccount_num(String account_num) {
		this.account_num = account_num;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getStaff_num() {
		return staff_num;
	}
	public void setStaff_num(String staff_num) {
		this.staff_num = staff_num;
	}
	public String getStaff_no() {
		return staff_no;
	}
	public void setStaff_no(String staff_no) {
		this.staff_no = staff_no;
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
	public String getStaff_name() {
		return staff_name;
	}
	public void setStaff_name(String staff_name) {
		this.staff_name = staff_name;
	}
	public String getDept_no() {
		return dept_no;
	}
	public void setDept_no(String dept_no) {
		this.dept_no = dept_no;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDeal_reason() {
		return deal_reason;
	}
	public void setDeal_reason(String deal_reason) {
		this.deal_reason = deal_reason;
	}
	public String getCount_flag() {
		return count_flag;
	}
	public void setCount_flag(String count_flag) {
		this.count_flag = count_flag;
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
	public double getInbound_c() {
		return inbound_c;
	}
	public void setInbound_c(double inbound_c) {
		BigDecimal b=new BigDecimal(inbound_c);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.inbound_c = f1;
	}
	public double getInbound_d() {
		return inbound_d;
	}
	public void setInbound_d(double inbound_d) {
		BigDecimal b=new BigDecimal(inbound_d);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.inbound_d = f1;
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
	public double getOutbound_c() {
		return outbound_c;
	}
	public void setOutbound_c(double outbound_c) {
		BigDecimal b=new BigDecimal(outbound_c);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.outbound_c = f1;
	}
	public double getOutbound_d() {
		return outbound_d;
	}
	public void setOutbound_d(double outbound_d) {
		BigDecimal b=new BigDecimal(outbound_d);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.outbound_d = f1;
	}
	public int getInbound_Num() {
		return inbound_Num;
	}
	public void setInbound_Num(int inbound_Num) {
		this.inbound_Num = inbound_Num;
	}
	public double getInbound_Money() {
		return inbound_Money;
	}
	public void setInbound_Money(double inbound_Money) {
		BigDecimal b=new BigDecimal(inbound_Money);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.inbound_Money = f1;
	}
	public int getOutbound_Num() {
		return outbound_Num;
	}
	public void setOutbound_Num(int outbound_Num) {
		this.outbound_Num = outbound_Num;
	}
	public double getOutbound_Money() {
		return outbound_Money;
	}
	public void setOutbound_Money(double outbound_Money) {
		BigDecimal b=new BigDecimal(outbound_Money);  
		double f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		this.outbound_Money = f1;
	}
	public int getInbound_a_c() {
		return inbound_a_c;
	}
	public void setInbound_a_c(int inbound_a_c) {
		this.inbound_a_c = inbound_a_c;
	}
	public int getInbound_b_c() {
		return inbound_b_c;
	}
	public void setInbound_b_c(int inbound_b_c) {
		this.inbound_b_c = inbound_b_c;
	}
	public int getInbound_c_c() {
		return inbound_c_c;
	}
	public void setInbound_c_c(int inbound_c_c) {
		this.inbound_c_c = inbound_c_c;
	}
	public int getInbound_d_c() {
		return inbound_d_c;
	}
	public void setInbound_d_c(int inbound_d_c) {
		this.inbound_d_c = inbound_d_c;
	}
	public int getOutbound_a_c() {
		return outbound_a_c;
	}
	public void setOutbound_a_c(int outbound_a_c) {
		this.outbound_a_c = outbound_a_c;
	}
	public int getOutbound_b_c() {
		return outbound_b_c;
	}
	public void setOutbound_b_c(int outbound_b_c) {
		this.outbound_b_c = outbound_b_c;
	}
	public int getOutbound_c_c() {
		return outbound_c_c;
	}
	public void setOutbound_c_c(int outbound_c_c) {
		this.outbound_c_c = outbound_c_c;
	}
	public int getOutbound_d_c() {
		return outbound_d_c;
	}
	public void setOutbound_d_c(int outbound_d_c) {
		this.outbound_d_c = outbound_d_c;
	}
	
	
	
	
	
	
	
}