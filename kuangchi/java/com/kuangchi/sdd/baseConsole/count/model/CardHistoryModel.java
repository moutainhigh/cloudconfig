package com.kuangchi.sdd.baseConsole.count.model;

public class CardHistoryModel {
	
	private Integer id;//id
	private String staff_name;//员工姓名
	private String staff_no;//员工工号,此处存staff_num
	private String card_num;//卡号
	private String operate;//卡片状态改变，如从挂失为解挂，此处为解挂状态码
	private String operate_time;//改变时间
	private String state_name;//状态名称
	private String dept_name;//部门名称
	private String create_user;//操作员
	
	
	public String getCreate_user() {
		return create_user;
	}
	public String getCard_num() {
		return card_num;
	}
	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
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
	public String getState_name() {
		return state_name;
	}
	public void setState_name(String state_name) {
		this.state_name = state_name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}
	public String getOperate_time() {
		return operate_time;
	}
	public void setOperate_time(String operate_time) {
		this.operate_time = operate_time;
	}
	
	
}
