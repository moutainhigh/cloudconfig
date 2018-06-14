package com.kuangchi.sdd.businessConsole.process.model;

public class TaskTransfered {
	
	private Integer id;
	private String process_definition_id;
	private String process_instance_id;
	private String task_definition_key;
	private String staff_num ;
	private String transfered_staff_num ;
  
	private String transfered_staff_name;
	private String staff_name;
	private String task_name;
	private String process_name;
	private String transfered_staff_dept;
	private String create_time;
	  
	  
	public String getTransfered_staff_name() {
		return transfered_staff_name;
	}
	public void setTransfered_staff_name(String transfered_staff_name) {
		this.transfered_staff_name = transfered_staff_name;
	}
	public String getStaff_name() {
		return staff_name;
	}
	public void setStaff_name(String staff_name) {
		this.staff_name = staff_name;
	}
	public String getTask_name() {
		return task_name;
	}
	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}
	public String getProcess_name() {
		return process_name;
	}
	public void setProcess_name(String process_name) {
		this.process_name = process_name;
	}
	public String getTransfered_staff_dept() {
		return transfered_staff_dept;
	}
	public void setTransfered_staff_dept(String transfered_staff_dept) {
		this.transfered_staff_dept = transfered_staff_dept;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getProcess_definition_id() {
		return process_definition_id;
	}
	public void setProcess_definition_id(String process_definition_id) {
		this.process_definition_id = process_definition_id;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getProcess_instance_id() {
		return process_instance_id;
	}
	public void setProcess_instance_id(String process_instance_id) {
		this.process_instance_id = process_instance_id;
	}

	public String getTask_definition_key() {
		return task_definition_key;
	}
	public void setTask_definition_key(String task_definition_key) {
		this.task_definition_key = task_definition_key;
	}
	public String getStaff_num() {
		return staff_num;
	}
	public void setStaff_num(String staff_num) {
		this.staff_num = staff_num;
	}
	public String getTransfered_staff_num() {
		return transfered_staff_num;
	}
	public void setTransfered_staff_num(String transfered_staff_num) {
		this.transfered_staff_num = transfered_staff_num;
	}
	  
	  
}
