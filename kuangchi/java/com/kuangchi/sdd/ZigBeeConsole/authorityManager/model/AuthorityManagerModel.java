package com.kuangchi.sdd.ZigBeeConsole.authorityManager.model;


/**
 * @创建人　: chudan.guo
 * @创建时间: 2016-10-18
 * @功能描述: 光子锁权限管理-model
 */
public class AuthorityManagerModel {
	private String staff_num;//员工编号
	private String authority_num; //权限编号
	private String device_id;//设备ID
	private String light_id;//光ID(员工绑定的卡号)
	private String number_code;//数字密码
	private String begin_valid_date;//开始有效时间
	private String end_valid_date;//结束有效时间
	private int transportFlag;// 标志位    0x01，还有下一组数据要传输；0x00，后续没有数据要传输
	private String authority_flag; //权限状态
	private String create_user; //创建人
	
	
	
	public String getCreate_user() {
		return create_user;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
	public String getAuthority_flag() {
		return authority_flag;
	}
	public void setAuthority_flag(String authority_flag) {
		this.authority_flag = authority_flag;
	}
	public String getStaff_num() {
		return staff_num;
	}
	public void setStaff_num(String staff_num) {
		this.staff_num = staff_num;
	}
	
	public int getTransportFlag() {
		return transportFlag;
	}
	public void setTransportFlag(int transportFlag) {
		this.transportFlag = transportFlag;
	}
	public String getAuthority_num() {
		return authority_num;
	}
	public void setAuthority_num(String authority_num) {
		this.authority_num = authority_num;
	}
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public String getLight_id() {
		return light_id;
	}
	public void setLight_id(String light_id) {
		this.light_id = light_id;
	}
	public String getNumber_code() {
		return number_code;
	}
	public void setNumber_code(String number_code) {
		this.number_code = number_code;
	}
	public String getBegin_valid_date() {
		return begin_valid_date;
	}
	public void setBegin_valid_date(String begin_valid_date) {
		this.begin_valid_date = begin_valid_date;
	}
	public String getEnd_valid_date() {
		return end_valid_date;
	}
	public void setEnd_valid_date(String end_valid_date) {
		this.end_valid_date = end_valid_date;
	}
	
}
