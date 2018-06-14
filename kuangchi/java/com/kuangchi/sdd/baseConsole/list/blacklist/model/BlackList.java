package com.kuangchi.sdd.baseConsole.list.blacklist.model;

import com.kuangchi.sdd.base.model.BaseModelSupport;



public class BlackList extends BaseModelSupport{
	private Integer black_id;//id
	private String staff_num;//员工编号
	private String staff_no;//员工工号
	private String staff_name;//员工名称
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
	private String card_num;//卡号
	private String validity_flag;//有效标志
	private String create_user;//创建人员代码
	private String create_time;//创建时间
	private String update_time;//修改时间
	private String description;//描述
	public Integer getBlack_id() {
		return black_id;
	}
	public void setBlack_id(Integer black_id) {
		this.black_id = black_id;
	}
	public String getStaff_num() {
		return staff_num;
	}
	public void setStaff_num(String staff_num) {
		this.staff_num = staff_num;
	}
	public String getCard_num() {
		return card_num;
	}
	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}
	public String getValidity_flag() {
		return validity_flag;
	}
	public void setValidity_flag(String validity_flag) {
		this.validity_flag = validity_flag;
	}
	public String getCreate_user() {
		return create_user;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
