package com.kuangchi.sdd.attendanceConsole.attend.model;

import com.kuangchi.sdd.base.model.BaseModelSupport;

public class AttendModel extends BaseModelSupport{

	private Integer id;
	private String staff_num;
	private String staff_no;
	private String staff_name;
	private String checkdate;
	private String checktime;
	private String device_id;
	private String device_name;
	private String device_mac;
	private String door_num;
	private String door_name;
	private String flag_status;
	private String create_time;
	private String from_time;
	private String to_time;
	private String order_flag;
	
	private String dept_num;
	private String layerDeptNum; // 分层部门代码，用于筛选显示部门	by yuman.gao
	
	
	
	public String getDept_num() {
		return dept_num;
	}
	public void setDept_num(String dept_num) {
		this.dept_num = dept_num;
	}
	public String getLayerDeptNum() {
		return layerDeptNum;
	}
	public void setLayerDeptNum(String layerDeptNum) {
		this.layerDeptNum = layerDeptNum;
	}
	
	
	/*private String jsDm; // 角色代码  用于分层	by yuman.gao
	private String yhDm; //用户代码
	
	
	public String getJsDm() {
		return jsDm;
	}

	public void setJsDm(String jsDm) {
		this.jsDm = jsDm;
	}

	public String getYhDm() {
		return yhDm;
	}

	public void setYhDm(String yhDm) {
		this.yhDm = yhDm;
	}*/

	public String getStaff_no() {
		return staff_no;
	}
	public void setStaff_no(String staff_no) {
		this.staff_no = staff_no;
	}
	public String getDevice_mac() {
		return device_mac;
	}
	public void setDevice_mac(String device_mac) {
		this.device_mac = device_mac;
	}
	public String getOrder_flag() {
		return order_flag;
	}
	public void setOrder_flag(String order_flag) {
		this.order_flag = order_flag;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getDoor_num() {
		return door_num;
	}
	public void setDoor_num(String door_num) {
		this.door_num = door_num;
	}
	public String getDoor_name() {
		return door_name;
	}
	public void setDoor_name(String door_name) {
		this.door_name = door_name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getChecktime() {
		return checktime;
	}
	public void setChecktime(String checktime) {
		this.checktime = checktime;
	}
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	public String getFlag_status() {
		return flag_status;
	}
	public void setFlag_status(String flag_status) {
		this.flag_status = flag_status;
	}
	public String getCheckdate() {
		return checkdate;
	}
	public void setCheckdate(String checkdate) {
		this.checkdate = checkdate;
	}
	public String getFrom_time() {
		return from_time;
	}
	public void setFrom_time(String from_time) {
		this.from_time = from_time;
	}
	public String getTo_time() {
		return to_time;
	}
	public void setTo_time(String to_time) {
		this.to_time = to_time;
	}
}
