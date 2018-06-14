package com.kuangchi.sdd.businessConsole.staffUser.model;

import java.io.Serializable;

public class Staff implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer staff_id;//ID号
	private String staff_password;//用户密码
	private String staff_num;//员工编号
	private String staff_no;//员工编号
	private String staff_name;//员工名字
	private String staff_age;//员工年龄
	private String staff_sex;//员工性别
	private String staff_address;//员工住址
	private String papers_type;//证件类型
	private String papers_num;//证件号码
	private String staff_mobile;//员工手机号
	private String staff_phone;//固定电话
	private String staff_position;//员工职位
	private String staff_email;//员工邮箱
	private String staff_img;//员工头像
	private String staff_dept;//所属部门
	private String lead_num;//直属领导编号
	private String staff_hiredate;//入职时间
	private String staff_state;//员工状态，1 正常  2 冻结
	private String staff_remark;//备注
	
	public String getStaff_no() {
		return staff_no;
	}
	public void setStaff_no(String staff_no) {
		this.staff_no = staff_no;
	}
	public Integer getStaff_id() {
		return staff_id;
	}
	public void setStaff_id(Integer staff_id) {
		this.staff_id = staff_id;
	}
	public String getStaff_password() {
		return staff_password;
	}
	public void setStaff_password(String staff_password) {
		this.staff_password = staff_password;
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
	public String getStaff_age() {
		return staff_age;
	}
	public void setStaff_age(String staff_age) {
		this.staff_age = staff_age;
	}
	public String getStaff_sex() {
		return staff_sex;
	}
	public void setStaff_sex(String staff_sex) {
		this.staff_sex = staff_sex;
	}
	public String getStaff_address() {
		return staff_address;
	}
	public void setStaff_address(String staff_address) {
		this.staff_address = staff_address;
	}
	public String getPapers_type() {
		return papers_type;
	}
	public void setPapers_type(String papers_type) {
		this.papers_type = papers_type;
	}
	public String getPapers_num() {
		return papers_num;
	}
	public void setPapers_num(String papers_num) {
		this.papers_num = papers_num;
	}
	public String getStaff_mobile() {
		return staff_mobile;
	}
	public void setStaff_mobile(String staff_mobile) {
		this.staff_mobile = staff_mobile;
	}
	public String getStaff_phone() {
		return staff_phone;
	}
	public void setStaff_phone(String staff_phone) {
		this.staff_phone = staff_phone;
	}
	public String getStaff_position() {
		return staff_position;
	}
	public void setStaff_position(String staff_position) {
		this.staff_position = staff_position;
	}
	public String getStaff_email() {
		return staff_email;
	}
	public void setStaff_email(String staff_email) {
		this.staff_email = staff_email;
	}
	public String getStaff_img() {
		return staff_img;
	}
	public void setStaff_img(String staff_img) {
		this.staff_img = staff_img;
	}
	public String getStaff_dept() {
		return staff_dept;
	}
	public void setStaff_dept(String staff_dept) {
		this.staff_dept = staff_dept;
	}
	public String getLead_num() {
		return lead_num;
	}
	public void setLead_num(String lead_num) {
		this.lead_num = lead_num;
	}
	public String getStaff_hiredate() {
		return staff_hiredate;
	}
	public void setStaff_hiredate(String staff_hiredate) {
		this.staff_hiredate = staff_hiredate;
	}
	public String getStaff_state() {
		return staff_state;
	}
	public void setStaff_state(String staff_state) {
		this.staff_state = staff_state;
	}
	public String getStaff_remark() {
		return staff_remark;
	}
	public void setStaff_remark(String staff_remark) {
		this.staff_remark = staff_remark;
	}
	
}
