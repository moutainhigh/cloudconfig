package com.kuangchi.sdd.businessConsole.process.model;

public class ProcessInstanceSubmit {
	 String id;
	 String process_definition_id;
	 String process_instance_id;
	 String staff_num;
	 Integer lock_status;
	 String create_time;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProcess_definition_id() {
		return process_definition_id;
	}
	public void setProcess_definition_id(String process_definition_id) {
		this.process_definition_id = process_definition_id;
	}
	public String getProcess_instance_id() {
		return process_instance_id;
	}
	public void setProcess_instance_id(String process_instance_id) {
		this.process_instance_id = process_instance_id;
	}
	public String getStaff_num() {
		return staff_num;
	}
	public void setStaff_num(String staff_num) {
		this.staff_num = staff_num;
	}

	public Integer getLock_status() {
		return lock_status;
	}
	public void setLock_status(Integer lock_status) {
		this.lock_status = lock_status;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	 
	 
	 
}
