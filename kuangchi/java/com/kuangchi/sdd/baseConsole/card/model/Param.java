package com.kuangchi.sdd.baseConsole.card.model;

public class Param {
	private String card_num;// 卡号
	private String staff_name;// 卡号
	
	private String value;// 条件值
	private String begin_time;// 开始时间
	private String end_time;// 结束时间

	private String device_name;// 设备名称
	private String staff_no; //员工工号
	private String dept_num;	//部门编号
	private String dept_name;	//部门名称
	
	
	
	public String getDept_name() {
		return dept_name;
	}

	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}

	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}

	public String getStaff_no() {
		return staff_no;
	}

	public String getDevice_name() {
		return device_name;
	}

	public void setStaff_no(String staff_no) {
		this.staff_no = staff_no;
	}

	public String getDept_num() {
		return dept_num;
	}

	public void setDept_num(String dept_num) {
		this.dept_num = dept_num;
	}

	public String getStaff_name() {
		return staff_name;
	}

	public void setStaff_name(String staff_name) {
		this.staff_name = staff_name;
	}

	public String getType_dm() {
		return type_dm;
	}

	public void setType_dm(String type_dm) {
		this.type_dm = type_dm;
	}

	private String state;// 状态
	private String type_dm;// 卡片类型代码

	public String getCard_num() {
		return card_num;
	}

	public void setCard_num(String card_num) {
		this.card_num = card_num;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
