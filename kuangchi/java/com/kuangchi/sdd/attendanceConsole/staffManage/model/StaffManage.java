package com.kuangchi.sdd.attendanceConsole.staffManage.model;

public class StaffManage {
	
	private String staff_id;//员工id
	private String staff_num;//员工编号
	private String staff_no;//员工工号（显示）
	private String staff_name;//员工名字
	private String staff_password;//用户密码
	private String bm_mc;//部门名称
	private String gw_mc;//岗位名称
	
	private String layerDeptNum; // 分层部门代码，用于筛选显示部门	by yuman.gao
	
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

	public String getStaff_id() {
		return staff_id;
	}
	public void setStaff_id(String staff_id) {
		this.staff_id = staff_id;
	}
	public String getStaff_num() {
		return staff_num;
	}
	public void setStaff_num(String staff_num) {
		this.staff_num = staff_num;
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
	public String getStaff_password() {
		return staff_password;
	}
	public void setStaff_password(String staff_password) {
		this.staff_password = staff_password;
	}
	public String getBm_mc() {
		return bm_mc;
	}
	public void setBm_mc(String bm_mc) {
		this.bm_mc = bm_mc;
	}
	public String getGw_mc() {
		return gw_mc;
	}
	public void setGw_mc(String gw_mc) {
		this.gw_mc = gw_mc;
	}
	
	
	
}
