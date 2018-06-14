package com.kuangchi.sdd.baseConsole.count.model;

/**
 * @创建人　: 肖红丽
 * @创建时间: 2016-4-6上午10:51:25
 * @功能描述:统计已发卡权限bean
 * @参数描述:
 */
public class CountCardAuthority {
	private Integer authority_id; //权限ID
	private String card_num;  //卡号
	
	private String staff_num; //员工代码
	private String staff_no; //员工代码
	private String staff_name; //员工姓名
	
	private String door_num;  //门编号
	private String door_name;//门名称
	private String device_ip;
	private String device_num;  //设备编号
	private String device_name; //设备名称
	
	private String validity_flag;	//有效标志
	private String create_user;	//创建人员代码
	private String	create_time; //创建时间
	private String description;	//描述
	private String valid_start_time; //有效期开始时间
	private String valid_end_time;	//有效期结束时间
	
	private String time_group_num;	//时段组编号
	private String time_group_name; //时段组名称
	

	public String getStaff_no() {
		return staff_no;
	}

	public void setStaff_no(String staff_no) {
		this.staff_no = staff_no;
	}

	public String getTime_group_name() {
		return time_group_name;
	}

	public void setTime_group_name(String time_group_name) {
		this.time_group_name = time_group_name;
	}

	public Integer getAuthority_id() {
		return authority_id;
	}

	public void setAuthority_id(Integer authority_id) {
		this.authority_id = authority_id;
	}

	public String getCard_num() {
		return card_num;
	}

	public void setCard_num(String card_num) {
		this.card_num = card_num;
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

	public String getDevice_ip() {
		return device_ip;
	}

	public void setDevice_ip(String device_ip) {
		this.device_ip = device_ip;
	}

	public String getDevice_num() {
		return device_num;
	}

	public void setDevice_num(String device_num) {
		this.device_num = device_num;
	}

	public String getDevice_name() {
		return device_name;
	}

	public void setDevice_name(String device_name) {
		this.device_name = device_name;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValid_start_time() {
		return valid_start_time;
	}

	public void setValid_start_time(String valid_start_time) {
		this.valid_start_time = valid_start_time;
	}

	public String getValid_end_time() {
		return valid_end_time;
	}

	public void setValid_end_time(String valid_end_time) {
		this.valid_end_time = valid_end_time;
	}

	public String getTime_group_num() {
		return time_group_num;
	}

	public void setTime_group_num(String time_group_num) {
		this.time_group_num = time_group_num;
	}
	
}
