package com.kuangchi.sdd.attendanceConsole.attend.model;

public class outworkModel {
	private Integer id;
	private String staff_num;//员工编号
	private String staff_no;//员工编号
	private String staff_name;//员工名字
	private String reason;//外出原因
	private String from_time;//开始时间
	private String to_time;//结束时间
	private Integer total_time;//外出时间
	private String process_instance_id;//流程实例ID
	private String begin_time;//开始时间
	private String end_time;//结束日期
	
	private String layerDeptNum; // 分层部门代码，用于筛选显示部门
	
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

	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	private String BM_NO;//部门代码
	private String BM_DM;//部门代码
	private String BM_MC;//部门名称
	private String create_time;//创建时间
	
	
	
	public String getBM_NO() {
		return BM_NO;
	}
	public void setBM_NO(String bM_NO) {
		BM_NO = bM_NO;
	}
	public String getStaff_no() {
		return staff_no;
	}
	public void setStaff_no(String staff_no) {
		this.staff_no = staff_no;
	}
	public String getBM_DM() {
		return BM_DM;
	}
	public void setBM_DM(String bM_DM) {
		BM_DM = bM_DM;
	}
	public String getBM_MC() {
		return BM_MC;
	}
	public void setBM_MC(String bM_MC) {
		BM_MC = bM_MC;
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
	
	public String getStaff_name() {
		return staff_name;
	}
	public void setStaff_name(String staff_name) {
		this.staff_name = staff_name;
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
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
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
	public Integer getTotal_time() {
		return total_time;
	}
	public void setTotal_time(Integer total_time) {
		this.total_time = total_time;
	}
	public String getProcess_instance_id() {
		return process_instance_id;
	}
	public void setProcess_instance_id(String process_instance_id) {
		this.process_instance_id = process_instance_id;
	}
	
}
