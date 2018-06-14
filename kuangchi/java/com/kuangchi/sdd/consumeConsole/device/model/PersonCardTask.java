package com.kuangchi.sdd.consumeConsole.device.model;

import java.math.BigDecimal;

import com.kuangchi.sdd.base.model.BaseModelSupport;

public class PersonCardTask extends BaseModelSupport {

	private String id;
	private String task_id;
	private String device_num;
	private String card_num;
	private String staff_id;
	private String staff_no;
	private String staff_num;
	private String staff_name;
	private BigDecimal balance;
	private String flag;
	private String create_time;
	private Integer try_times;
	private Integer success_flag;
	private Integer trigger_flag;
	private String priority;

	public Integer getTry_times() {
		return try_times;
	}

	public void setTry_times(Integer try_times) {
		this.try_times = try_times;
	}

	public Integer getSuccess_flag() {
		return success_flag;
	}

	public void setSuccess_flag(Integer success_flag) {
		this.success_flag = success_flag;
	}

	public Integer getTrigger_flag() {
		return trigger_flag;
	}

	public void setTrigger_flag(Integer trigger_flag) {
		this.trigger_flag = trigger_flag;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
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

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

}
