package com.kuangchi.sdd.attendanceConsole.myduty.model;

import com.kuangchi.sdd.base.model.BaseModelSupport;

public class DutyUser extends BaseModelSupport {
	private Integer id;//员工排班id
	private String staff_num;//员工代码（工号）
	private String staff_no;//员工代码（工号）
	private String staff_name;//员工姓名
	private Integer valid_flag;//标记
	private String begin_time;//排班开始时间
	private String end_time;//排班结束时间
	private Integer duty_id;//排班类型id
	private String duty_name;//班次名称
	
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
