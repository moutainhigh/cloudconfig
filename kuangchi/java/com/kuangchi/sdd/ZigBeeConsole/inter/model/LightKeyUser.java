package com.kuangchi.sdd.ZigBeeConsole.inter.model;

public class LightKeyUser {
	private String userid;//用户ID
	private String user_phone; //手机号
	private String user_pwd; //密码
	private String realname; //真实姓名
	private String card_id;
	private String imei_code; //手机设备号
	private String photo; //用户头像
	private String companyid; //企业ID
	private String state; //用户状态（0正常   1冻结   2未授权）
	private String reg_time; //注册时间
	
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUser_phone() {
		return user_phone;
	}
	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}
	public String getUser_pwd() {
		return user_pwd;
	}
	public void setUser_pwd(String user_pwd) {
		this.user_pwd = user_pwd;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public String getImei_code() {
		return imei_code;
	}
	public void setImei_code(String imei_code) {
		this.imei_code = imei_code;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getCompanyid() {
		return companyid;
	}
	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getReg_time() {
		return reg_time;
	}
	public void setReg_time(String reg_time) {
		this.reg_time = reg_time;
	}
	
	
}
