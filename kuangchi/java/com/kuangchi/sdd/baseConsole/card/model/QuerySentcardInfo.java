package com.kuangchi.sdd.baseConsole.card.model;

public class QuerySentcardInfo {
	private String emp_name;//员工姓名
	private String staff_no;
	private String emp_num;
	private String emp_dept;//员工部门
	private String bm_no;
	private String emp_position;//员工岗位
	private String card_num;//卡号
	private String card_state;//卡片状态
	private String binding_time;//绑卡时间
	private String cell_id;//小区号
	public String getCell_id() {
		return cell_id;
	}
	public void setCell_id(String cell_id) {
		this.cell_id = cell_id;
	}
	public String getCard_num() {
		return card_num;
	}
	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}
	public String getEmp_name() {
		return emp_name;
	}
	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
	}
	public String getEmp_dept() {
		return emp_dept;
	}
	public void setEmp_dept(String emp_dept) {
		this.emp_dept = emp_dept;
	}
	public String getEmp_position() {
		return emp_position;
	}
	public void setEmp_position(String emp_position) {
		this.emp_position = emp_position;
	}
	public String getBinding_time() {
		return binding_time;
	}
	public void setBinding_time(String binding_time) {
		this.binding_time = binding_time;
	}
	public String getEmp_num() {
		return emp_num;
	}
	public void setEmp_num(String emp_num) {
		this.emp_num = emp_num;
	}
	public String getStaff_no() {
		return staff_no;
	}
	public void setStaff_no(String staff_no) {
		this.staff_no = staff_no;
	}
	public String getBm_no() {
		return bm_no;
	}
	public void setBm_no(String bm_no) {
		this.bm_no = bm_no;
	}
	public String getCard_state() {
		return card_state;
	}
	public void setCard_state(String card_state) {
		this.card_state = card_state;
	}
	
}
