package com.kuangchi.sdd.attendanceConsole.noCheckCard.model;



public class NoCheckCard {
	private Integer id;
	private String staff_num;//员工代码
	private String staff_no;//员工编号
	private String staff_name;//员工姓名
	private String BM_DM;//部门代码
	private String BM_MC;//部门名称
	private Integer check_point;//免打卡时间点
	private String create_time;//创建时间
	private String create_user;//创建者
	private String from_time;//开始时间
	private String to_time;//结束时间
	private String dept_num;//部门代码
	private String BM_NO;//部门编号
	private String herited_to_sub_dept;//是否应用于子部门
	private String generate_record; // 是否产生统计数据
	
	private String nocheck1;//上午上班
	private String nocheck2;//上午下班
	private String nocheck3;//下午上班
	private String nocheck4;//下午下班
	private String is_always; //一直免打卡
	
	
	public String remark; // 备注
	
	private String layerDeptNum; // 分层部门代码，用于筛选显示部门	by yuman.gao
	
	public String getLayerDeptNum() {
		return layerDeptNum;
	}
	public void setLayerDeptNum(String layerDeptNum) {
		this.layerDeptNum = layerDeptNum;
	}
	
	
	
	public String getIs_always() {
		return is_always;
	}
	public void setIs_always(String is_always) {
		this.is_always = is_always;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getGenerate_record() {
		return generate_record;
	}
	public void setGenerate_record(String generate_record) {
		this.generate_record = generate_record;
	}
	public String getHerited_to_sub_dept() {
		return herited_to_sub_dept;
	}
	public void setHerited_to_sub_dept(String herited_to_sub_dept) {
		this.herited_to_sub_dept = herited_to_sub_dept;
	}
	public String getBM_NO() {
		return BM_NO;
	}
	public void setBM_NO(String bM_NO) {
		BM_NO = bM_NO;
	}
	public String getDept_num() {
		return dept_num;
	}
	public void setDept_num(String dept_num) {
		this.dept_num = dept_num;
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
	public String getNocheck1() {
		return nocheck1;
	}
	public void setNocheck1(String nocheck1) {
		this.nocheck1 = nocheck1;
	}
	public String getNocheck2() {
		return nocheck2;
	}
	public void setNocheck2(String nocheck2) {
		this.nocheck2 = nocheck2;
	}
	public String getNocheck3() {
		return nocheck3;
	}
	public void setNocheck3(String nocheck3) {
		this.nocheck3 = nocheck3;
	}
	public String getNocheck4() {
		return nocheck4;
	}
	public void setNocheck4(String nocheck4) {
		this.nocheck4 = nocheck4;
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
	public String getBM_MC() {
		return BM_MC;
	}
	public void setBM_MC(String bM_MC) {
		BM_MC = bM_MC;
	}
	
	public String getBM_DM() {
		return BM_DM;
	}
	public void setBM_DM(String bM_DM) {
		BM_DM = bM_DM;
	}
	
	public Integer getCheck_point() {
		return check_point;
	}
	public void setCheck_point(Integer check_point) {
		this.check_point = check_point;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getCreate_user() {
		return create_user;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
	
	
	
	
}
