package com.kuangchi.sdd.consumeConsole.balanceAccount.model;

import java.math.BigDecimal;

public class BalanceAccountModel {
	private int id;
	private String every_date;//日期
	private String staff_num;//员工编号
	private String staff_name;//员工编号
	private String staff_no;	//员工工号
	private String previous_time;//上一次统计点时间
	private BigDecimal previous_balance;//上一次统计点余额
	private String current_time;//当前统计点时间
	private BigDecimal current_balance;//当前统计点余额
	private String create_time;//创建时间
	private String delete_flag;//删除标志
	private String account_num;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEvery_date() {
		return every_date;
	}
	public void setEvery_date(String every_date) {
		this.every_date = every_date;
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
	public String getStaff_no() {
		return staff_no;
	}
	public void setStaff_no(String staff_no) {
		this.staff_no = staff_no;
	}
	public String getPrevious_time() {
		return previous_time;
	}
	public void setPrevious_time(String previous_time) {
		this.previous_time = previous_time;
	}
	public BigDecimal getPrevious_balance() {
		return previous_balance;
	}
	public void setPrevious_balance(BigDecimal previous_balance) {
		this.previous_balance = previous_balance;
	}
	public String getCurrent_time() {
		return current_time;
	}
	public void setCurrent_time(String current_time) {
		this.current_time = current_time;
	}
	public BigDecimal getCurrent_balance() {
		return current_balance;
	}
	public void setCurrent_balance(BigDecimal current_balance) {
		this.current_balance = current_balance;
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
	public String getAccount_num() {
		return account_num;
	}
	public void setAccount_num(String account_num) {
		this.account_num = account_num;
	}
	
}
