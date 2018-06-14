package com.kuangchi.sdd.baseConsole.doorinfo.model;

public class DoorInfoModel {
	private Integer door_id;//门ID号
	private String door_num;//门编号
	private String old_door_num;//
	private String device_num;//设备编号
	private String old_device_num;//
	private String door_name;//门名称
	private String validity_flag;//有效标志为0
	private String create_user;//创建人员
	private String create_time;//创建时间
	private String description;//描述
	private String device_name;
	private String hadden_flag;//屏蔽标志位
	private String open_delay;//开门延时
	private String open_overtime;//开门超时
	private Integer first_open;//是否设置首卡开门
	private Integer use_super_password;//是否启用超级开门密码
	private String super_password;//超级开门密码
	private Integer use_force_password;//是否启用胁迫密码
	private String force_password;//胁迫密码
	private String relock_password;//重锁密码
	private String unlock_password;//解锁密码
	private String police_password;//报警密码
	private String open_pattern;//功能模式
	private String check_open_pattern;//验证开门模式
	private String work_pattern;//工作模式
	private Integer multi_open_number;//多卡开门数量
	private String multi_open_card_num;//多卡开门卡号
	private String local_ip_address;
	
	
	public String getLocal_ip_address() {
		return local_ip_address;
	}
	public void setLocal_ip_address(String local_ip_address) {
		this.local_ip_address = local_ip_address;
	}
	public String getHadden_flag() {
		return hadden_flag;
	}
	public void setHadden_flag(String hadden_flag) {
		this.hadden_flag = hadden_flag;
	}
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	
	public Integer getDoor_id() {
		return door_id;
	}
	public void setDoor_id(Integer door_id) {
		this.door_id = door_id;
	}
	public String getDoor_num() {
		return door_num;
	}
	public void setDoor_num(String door_num) {
		this.door_num = door_num;
	}
	public String getDevice_num() {
		return device_num;
	}
	public void setDevice_num(String device_num) {
		this.device_num = device_num;
	}
	public String getDoor_name() {
		return door_name;
	}
	public void setDoor_name(String door_name) {
		this.door_name = door_name;
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
	public String getOld_device_num() {
		return old_device_num;
	}
	public void setOld_device_num(String old_device_num) {
		this.old_device_num = old_device_num;
	}
	public String getOld_door_num() {
		return old_door_num;
	}
	public void setOld_door_num(String old_door_num) {
		this.old_door_num = old_door_num;
	}
	public String getOpen_delay() {
		return open_delay;
	}
	public void setOpen_delay(String open_delay) {
		this.open_delay = open_delay;
	}
	public String getOpen_overtime() {
		return open_overtime;
	}
	public void setOpen_overtime(String open_overtime) {
		this.open_overtime = open_overtime;
	}
	public Integer getFirst_open() {
		return first_open;
	}
	public void setFirst_open(Integer first_open) {
		this.first_open = first_open;
	}
	public Integer getUse_super_password() {
		return use_super_password;
	}
	public void setUse_super_password(Integer use_super_password) {
		this.use_super_password = use_super_password;
	}
	public String getSuper_password() {
		return super_password;
	}
	public void setSuper_password(String super_password) {
		this.super_password = super_password;
	}
	public Integer getUse_force_password() {
		return use_force_password;
	}
	public void setUse_force_password(Integer use_force_password) {
		this.use_force_password = use_force_password;
	}
	public String getForce_password() {
		return force_password;
	}
	public void setForce_password(String force_password) {
		this.force_password = force_password;
	}
	public String getRelock_password() {
		return relock_password;
	}
	public void setRelock_password(String relock_password) {
		this.relock_password = relock_password;
	}
	public String getUnlock_password() {
		return unlock_password;
	}
	public void setUnlock_password(String unlock_password) {
		this.unlock_password = unlock_password;
	}
	public String getPolice_password() {
		return police_password;
	}
	public void setPolice_password(String police_password) {
		this.police_password = police_password;
	}
	public String getOpen_pattern() {
		return open_pattern;
	}
	public void setOpen_pattern(String open_pattern) {
		this.open_pattern = open_pattern;
	}
	public String getCheck_open_pattern() {
		return check_open_pattern;
	}
	public void setCheck_open_pattern(String check_open_pattern) {
		this.check_open_pattern = check_open_pattern;
	}
	public String getWork_pattern() {
		return work_pattern;
	}
	public void setWork_pattern(String work_pattern) {
		this.work_pattern = work_pattern;
	}
	public Integer getMulti_open_number() {
		return multi_open_number;
	}
	public void setMulti_open_number(Integer multi_open_number) {
		this.multi_open_number = multi_open_number;
	}
	public String getMulti_open_card_num() {
		return multi_open_card_num;
	}
	public void setMulti_open_card_num(String multi_open_card_num) {
		this.multi_open_card_num = multi_open_card_num;
	}
	
	
}
