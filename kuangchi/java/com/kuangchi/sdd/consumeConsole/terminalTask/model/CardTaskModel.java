package com.kuangchi.sdd.consumeConsole.terminalTask.model;

import java.sql.Date;


public class CardTaskModel {
	private Integer id;
	private String task_id;//任务id
	private String device_num;//设备编号
	private String device_name;//设备名称
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	private String card_num;//卡号
	private String staff_id;//员工id
	private String staff_num;
	private String staff_no;//员工工号
	private String staff_name;//员工姓名
	private String try_times;//尝试次数
	public String getTry_times() {
		return try_times;
	}
	public void setTry_times(String try_times) {
		this.try_times = try_times;
	}
	private double balance;//卡片余额
	private Integer flag;//名单标志 0有效 1删除
	private String create_time;//创建时间
	private String trigger_flag;//触发条件 
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTask_id() {
		return task_id;
	}
	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}
	public String getDevice_num() {
		return device_num;
	}
	public void setDevice_num(String device_num) {
		this.device_num = device_num;
	}
	public String getCard_num() {
		return card_num;
	}
	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}
	public String getStaff_id() {
		return staff_id;
	}
	public void setStaff_id(String staff_id) {
		this.staff_id = staff_id;
	}
	public String getStaff_no() {
		return staff_no;
	}
	public void setStaff_no(String staff_no) {
		this.staff_no = staff_no;
	}
	public String getStaff_name() {
		return staff_name;
	}
	public void setStaff_name(String staff_name) {
		this.staff_name = staff_name;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public String getStaff_num() {
		return staff_num;
	}
	public void setStaff_num(String staff_num) {
		this.staff_num = staff_num;
	}
	public String getTrigger_flag() {
		return trigger_flag;
	}
	public void setTrigger_flag(String trigger_flag) {
		this.trigger_flag = trigger_flag;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	
}
