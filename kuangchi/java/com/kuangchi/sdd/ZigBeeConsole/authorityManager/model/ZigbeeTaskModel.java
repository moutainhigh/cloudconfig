package com.kuangchi.sdd.ZigBeeConsole.authorityManager.model;


/**
 * @创建人　: chudan.guo
 * @创建时间: 2016-10-18
 * @功能描述: 光子锁任务表-model
 */
public class ZigbeeTaskModel {
	private String id;
	private String authority_num;//权限编号
	private String task_state;//任务状态（0成功 1等待 2失败 默认为1）
	private String task_type;//0 下发权限 1删除权限
	private String create_time;//创建时间
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAuthority_num() {
		return authority_num;
	}
	public void setAuthority_num(String authority_num) {
		this.authority_num = authority_num;
	}
	public String getTask_state() {
		return task_state;
	}
	public void setTask_state(String task_state) {
		this.task_state = task_state;
	}
	public String getTask_type() {
		return task_type;
	}
	public void setTask_type(String task_type) {
		this.task_type = task_type;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	
	
}