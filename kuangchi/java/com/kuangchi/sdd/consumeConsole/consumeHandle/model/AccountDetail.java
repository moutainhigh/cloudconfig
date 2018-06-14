package com.kuangchi.sdd.consumeConsole.consumeHandle.model;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-8-24 下午4:46:48
 * @功能描述: 账户交易明细流水表-model
 */
public class AccountDetail {
	private String id; 
	private String detail_flow_no; //明细流水号
	private String account_num; //账号
	private String time; //时间
	private String staff_no; //员工编号（显示）
	private String staff_num; //员工编号
	private String staff_name; //员工名称
	private String dept_no; //部门编号（显示）
	private String dept_num; //部门编号
	private String dept_name; //部门名称
	private String type; //收支类型
	private Double inbound; //收入金额
	private Double outbound; //支出金额
	private Double balance; //余额
	private String count_flag; //账户标志
	private String deal_reason; //交易原因
	
	
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
	public String getAccount_num() {
		return account_num;
	}
	public void setAccount_num(String account_num) {
		this.account_num = account_num;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDetail_flow_no() {
		return detail_flow_no;
	}
	public void setDetail_flow_no(String detail_flow_no) {
		this.detail_flow_no = detail_flow_no;
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
	public String getStaff_name() {
		return staff_name;
	}
	public void setStaff_name(String staff_name) {
		this.staff_name = staff_name;
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
	public Double getInbound() {
		return inbound;
	}
	public void setInbound(Double inbound) {
		this.inbound = inbound;
	}
	public Double getOutbound() {
		return outbound;
	}
	public void setOutbound(Double outbound) {
		this.outbound = outbound;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public String getCount_flag() {
		return count_flag;
	}
	public void setCount_flag(String count_flag) {
		this.count_flag = count_flag;
	}
	public String getDeal_reason() {
		return deal_reason;
	}
	public void setDeal_reason(String deal_reason) {
		this.deal_reason = deal_reason;
	}
	
	
}
