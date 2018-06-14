package com.kuangchi.sdd.baseConsole.card.model;

public abstract class BaseModelSupport {
	private String validity_flag;//有效标识
	private String create_user;//创建人员代码
	private String description;//描述
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
