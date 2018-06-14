package com.kuangchi.sdd.businessConsole.employee.model;

public class EmployeeTreeNode {
	  private String UUID;
	  private String yhDm;// 员工编号
	  private String yhMc;// 员工名称
	  private String bmDm;// 主属部门代码
	  private String xb;//性别
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public String getYhDm() {
		return yhDm;
	}
	public void setYhDm(String yhDm) {
		this.yhDm = yhDm;
	}
	public String getYhMc() {
		return yhMc;
	}
	public void setYhMc(String yhMc) {
		this.yhMc = yhMc;
	}
	public String getBmDm() {
		return bmDm;
	}
	public void setBmDm(String bmDm) {
		this.bmDm = bmDm;
	}
	public String getXb() {
		return xb;
	}
	public void setXb(String xb) {
		this.xb = xb;
	}
	  
}
