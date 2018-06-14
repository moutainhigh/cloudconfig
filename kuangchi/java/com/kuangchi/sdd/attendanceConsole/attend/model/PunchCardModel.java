package com.kuangchi.sdd.attendanceConsole.attend.model;

import com.kuangchi.sdd.base.model.BaseModelSupport;

public class PunchCardModel extends BaseModelSupport{

	private Integer id;
	private String duty_id;
	private String staff_num;
	private String staff_no;
	private String staff_name;
	private String duty_name;
	private String begin_time;
	private String end_time;
	private String BM_MC;
	private String dept_num;
	
	private String layerDeptNum;
	private String is_elastic;
	
	private String create_time;
	
	
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getIs_elastic() {
		return is_elastic;
	}
	public void setIs_elastic(String is_elastic) {
		this.is_elastic = is_elastic;
	}
	
	public String getLayerDeptNum() {
		return layerDeptNum;
	}
	public void setLayerDeptNum(String layerDeptNum) {
		this.layerDeptNum = layerDeptNum;
	}
	private String duty_time1;
	private String duty_time2;
	private String duty_time3;
	private String duty_time4;
	private String elastic_default_duty_time1;
	private String elastic_default_duty_time2;
	
	
	private String checkdate;//补打卡时间
	private String checktime;//补打卡时间点
	private String flag_status;
	
	
	
	
	public String getElastic_default_duty_time1() {
		return elastic_default_duty_time1;
	}
	public void setElastic_default_duty_time1(String elastic_default_duty_time1) {
		this.elastic_default_duty_time1 = elastic_default_duty_time1;
	}
	public String getElastic_default_duty_time2() {
		return elastic_default_duty_time2;
	}
	public void setElastic_default_duty_time2(String elastic_default_duty_time2) {
		this.elastic_default_duty_time2 = elastic_default_duty_time2;
	}
	public String getDuty_id() {
		return duty_id;
	}
	public void setDuty_id(String duty_id) {
		this.duty_id = duty_id;
	}
	public String getDept_num() {
		return dept_num;
	}
	public void setDept_num(String dept_num) {
		this.dept_num = dept_num;
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
	public String getChecktime() {
		return checktime;
	}
	public void setChecktime(String checktime) {
		this.checktime = checktime;
	}
	public String getDuty_time1() {
		return duty_time1;
	}
	public void setDuty_time1(String duty_time1) {
		this.duty_time1 = duty_time1;
	}
	public String getDuty_time2() {
		return duty_time2;
	}
	public void setDuty_time2(String duty_time2) {
		this.duty_time2 = duty_time2;
	}
	public String getDuty_time3() {
		return duty_time3;
	}
	public void setDuty_time3(String duty_time3) {
		this.duty_time3 = duty_time3;
	}
	public String getDuty_time4() {
		return duty_time4;
	}
	public void setDuty_time4(String duty_time4) {
		this.duty_time4 = duty_time4;
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
	public String getBM_MC() {
		return BM_MC;
	}
	public void setBM_MC(String bM_MC) {
		BM_MC = bM_MC;
	}
	
	

	
}
