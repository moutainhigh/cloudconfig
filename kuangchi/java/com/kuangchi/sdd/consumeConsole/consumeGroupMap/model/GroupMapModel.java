package com.kuangchi.sdd.consumeConsole.consumeGroupMap.model;

public class GroupMapModel {
	
	private String id; 
	private String group_num; //消费组代码
	private String group_name; //消费组名称
	private String staff_num; //绑定员工编号
	private String staff_no; //绑定员工工号 （显示用）
	private String staff_name; //员工姓名
	private String staff_dept; //员工所属部门名称
	private String create_time; //创建时间
	
	public String getStaff_no() {
		return staff_no;
	}
	public void setStaff_no(String staff_no) {
		this.staff_no = staff_no;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getStaff_dept() {
		return staff_dept;
	}
	public void setStaff_dept(String staff_dept) {
		this.staff_dept = staff_dept;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	
	
	
}
