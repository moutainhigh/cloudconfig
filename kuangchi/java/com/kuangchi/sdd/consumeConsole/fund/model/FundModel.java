package com.kuangchi.sdd.consumeConsole.fund.model;


/**
 * @创建人　: 高育漫
 * @创建时间: 2016-8-16 上午11:04:37
 * @功能描述: 资金明细表 Model
 */

public class FundModel {
	
	private Integer id;
	private Double amount; //额度
	private String organiztion_num; //机构编号
	private String organiztion_name; //机构名称
	private String card_num; //卡号
	private String staff_num; //员工编号
	private String staff_name; //员工名称
	private String dept_num; //部门编号
	private String dept_name; //部门名称
	private String account_type_num; //账户类型编号
	private String account_type_name; //账户类型名称
	private String op_date; //操作时间
	private String op_type; //操作类型
	private Double balance; //余额
	private String op_id; //用户ID
	private String reason; //原因
	
	
	public String getAccount_type_num() {
		return account_type_num;
	}
	public void setAccount_type_num(String account_type_num) {
		this.account_type_num = account_type_num;
	}
	public String getAccount_type_name() {
		return account_type_name;
	}
	public void setAccount_type_name(String account_type_name) {
		this.account_type_name = account_type_name;
	}
	public String getStaff_num() {
		return staff_num;
	}
	public void setStaff_num(String staff_num) {
		this.staff_num = staff_num;
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
	public String getOrganiztion_name() {
		return organiztion_name;
	}
	public void setOrganiztion_name(String organiztion_name) {
		this.organiztion_name = organiztion_name;
	}
	public String getStaff_name() {
		return staff_name;
	}
	public void setStaff_name(String staff_name) {
		this.staff_name = staff_name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOrganiztion_num() {
		return organiztion_num;
	}
	public void setOrganiztion_num(String organiztion_num) {
		this.organiztion_num = organiztion_num;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getCard_num() {
		return card_num;
	}
	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}
	public String getOp_date() {
		return op_date;
	}
	public void setOp_date(String op_date) {
		this.op_date = op_date;
	}
	public String getOp_type() {
		return op_type;
	}
	public void setOp_type(String op_type) {
		this.op_type = op_type;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public String getOp_id() {
		return op_id;
	}
	public void setOp_id(String op_id) {
		this.op_id = op_id;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
}
