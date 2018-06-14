package com.kuangchi.sdd.attendanceConsole.dutyuser.model;

import com.kuangchi.sdd.base.model.BaseModelSupport;

public class DutyUser extends BaseModelSupport implements Cloneable {
	private Integer id;//员工排班id
	private String staff_num;//员工编号（工号）
	private String staff_no;//员工工号（工号）
	private String staff_name;//员工姓名
	private Integer valid_flag;//标记
	private String begin_time;//排班开始时间
	private String end_time;//排班结束时间
	private Integer duty_id;//排班类型id
	private String duty_name;//班次名称
	private String dept_num;//部门代码

	private String flag;//是否跨天标志  0:否  1:是
	
	
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
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






	//for 对象浅复制
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	
	
	


	





	public String getDept_num() {
		return dept_num;
	}



	public void setDept_num(String dept_num) {
		this.dept_num = dept_num;
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
	public String getDuty_name() {
		return duty_name;
	}
	public void setDuty_name(String duty_name) {
		this.duty_name = duty_name;
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
	public Integer getDuty_id() {
		return duty_id;
	}
	public void setDuty_id(Integer duty_id) {
		this.duty_id = duty_id;
	}
	
	public Integer getValid_flag() {
		return valid_flag;
	}
	public void setValid_flag(Integer valid_flag) {
		this.valid_flag = valid_flag;
	}
	
}
