package com.kuangchi.sdd.attendanceConsole.attendCountType.model;


public class StaffAttendCount {
	
	private String bm_dm;//部门编号
	private String bm_mc;//部门名称
	private String sjbm_mc;//部门名称
	private String staff_num;
	private String staff_name;
	private String staff_no;
	private Double kg_time;	//旷工时间
	private Double early_time; 		//早退时间
	private Double later_time; 		//迟到时间
	private String isvocation; 		//是否公休日 	0 否  1是
	private String isholiday; 		//是否节假日	0 否  1是
	private String every_date; 		//日期
	private String check_time1; //上班刷卡时间
	private String check_device1; // 上班刷卡设备
	private String check_time2; // 下班刷卡时间
	private String check_device2; // 下班刷卡设备
	private String isKg;//是否旷工  	0 否  1是
	private String isLater;//是否迟到 	0 否  1是
	private String isEarly;//是否早退	 0 否  1是
	private String remark; // 备注
	
	private String layerDeptNum; // 分层部门代码	by yuman.gao

	public String getBm_dm() {
		return bm_dm;
	}

	public void setBm_dm(String bm_dm) {
		this.bm_dm = bm_dm;
	}

	public String getBm_mc() {
		return bm_mc;
	}

	public void setBm_mc(String bm_mc) {
		this.bm_mc = bm_mc;
	}

	public String getSjbm_mc() {
		return sjbm_mc;
	}

	public void setSjbm_mc(String sjbm_mc) {
		this.sjbm_mc = sjbm_mc;
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

	public String getStaff_no() {
		return staff_no;
	}

	public void setStaff_no(String staff_no) {
		this.staff_no = staff_no;
	}

	public Double getKg_time() {
		return kg_time;
	}

	public void setKg_time(Double kg_time) {
		this.kg_time = kg_time;
	}

	public Double getEarly_time() {
		return early_time;
	}

	public void setEarly_time(Double early_time) {
		this.early_time = early_time;
	}

	public Double getLater_time() {
		return later_time;
	}

	public void setLater_time(Double later_time) {
		this.later_time = later_time;
	}

	public String getIsvocation() {
		return isvocation;
	}

	public void setIsvocation(String isvocation) {
		this.isvocation = isvocation;
	}

	public String getIsholiday() {
		return isholiday;
	}

	public void setIsholiday(String isholiday) {
		this.isholiday = isholiday;
	}

	public String getEvery_date() {
		return every_date;
	}

	public void setEvery_date(String every_date) {
		this.every_date = every_date;
	}

	public String getCheck_time1() {
		return check_time1;
	}

	public void setCheck_time1(String check_time1) {
		this.check_time1 = check_time1;
	}

	public String getCheck_device1() {
		return check_device1;
	}

	public void setCheck_device1(String check_device1) {
		this.check_device1 = check_device1;
	}

	public String getCheck_time2() {
		return check_time2;
	}

	public void setCheck_time2(String check_time2) {
		this.check_time2 = check_time2;
	}

	public String getCheck_device2() {
		return check_device2;
	}

	public void setCheck_device2(String check_device2) {
		this.check_device2 = check_device2;
	}

	public String getIsKg() {
		return isKg;
	}

	public void setIsKg(String isKg) {
		this.isKg = isKg;
	}

	public String getIsLater() {
		return isLater;
	}

	public void setIsLater(String isLater) {
		this.isLater = isLater;
	}

	public String getIsEarly() {
		return isEarly;
	}

	public void setIsEarly(String isEarly) {
		this.isEarly = isEarly;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getLayerDeptNum() {
		return layerDeptNum;
	}

	public void setLayerDeptNum(String layerDeptNum) {
		this.layerDeptNum = layerDeptNum;
	}
	
	
	
}
