package com.kuangchi.sdd.businessConsole.randomCode.model;

public class RandomCode {
	private String id;
	private String staffMail;//用户工号
	private String randCode;//6位验证码
	private String validTime;//验证码有效时间
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	public String getValidTime() {
		return validTime;
	}
	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}
	
	public String getStaffMail() {
		return staffMail;
	}
	public void setStaffMail(String staffMail) {
		this.staffMail = staffMail;
	}
	public String getRandCode() {
		return randCode;
	}
	public void setRandCode(String randCode) {
		this.randCode = randCode;
	}
	
	
}
