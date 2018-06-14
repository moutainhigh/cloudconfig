package com.kuangchi.sdd.businessConsole.cron.model;

/**
 * 页面定时任务执行ip
 * @author minting.he
 *
 */
public class Cron {
	private String id;
	private String sys_key;	//页面定时任务执行的ip地址
	private String sys_value;
	private String description;
	
	public Cron() {
		super();
	}

	public Cron(String id, String sys_key, String sys_value, String description) {
		super();
		this.id = id;
		this.sys_key = sys_key;
		this.sys_value = sys_value;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSys_key() {
		return sys_key;
	}

	public void setSys_key(String sys_key) {
		this.sys_key = sys_key;
	}

	public String getSys_value() {
		return sys_value;
	}

	public void setSys_value(String sys_value) {
		this.sys_value = sys_value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}
