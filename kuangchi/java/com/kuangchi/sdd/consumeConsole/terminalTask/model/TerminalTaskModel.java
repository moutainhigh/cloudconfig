package com.kuangchi.sdd.consumeConsole.terminalTask.model;

import java.sql.Date;


public class TerminalTaskModel {
	private String id;
	private Integer priority;//优先级
	private String task_name;//任务名称
	private String task_type;//任务类型
	private Integer flag;//标志 0有效 1删除
	private Integer subtask_done_count;//已完成子任务个数
	private Integer subtask_count;//分任务总数
	private String create_time;//创建时间
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getTask_name() {
		return task_name;
	}
	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}
	public String getTask_type() {
		return task_type;
	}
	public void setTask_type(String task_type) {
		this.task_type = task_type;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public Integer getSubtask_done_count() {
		return subtask_done_count;
	}
	public void setSubtask_done_count(Integer subtask_done_count) {
		this.subtask_done_count = subtask_done_count;
	}
	public Integer getSubtask_count() {
		return subtask_count;
	}
	public void setSubtask_count(Integer subtask_count) {
		this.subtask_count = subtask_count;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	
}
