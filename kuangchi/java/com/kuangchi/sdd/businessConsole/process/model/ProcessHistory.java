package com.kuangchi.sdd.businessConsole.process.model;

import com.kuangchi.sdd.base.model.BaseModelSupport;

public class ProcessHistory extends BaseModelSupport{
	   String id;
	   String  process_definition_id;
	   String  process_definition_name;
	   String  process_instance_id;
	   String  task_definition_key;
	   String task_name;
	   String  staff_num;
	   String  create_time;
	   String lock_status;
	   String starter_name;
	   String department_name;
	   String variable_grasp;
	   String processDefinitionIds;
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
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getProcess_definition_name() {
		return process_definition_name;
	}
	public void setProcess_definition_name(String process_definition_name) {
		this.process_definition_name = process_definition_name;
	}
	public String getLock_status() {
		return lock_status;
	}
	public void setLock_status(String lock_status) {
		this.lock_status = lock_status;
	}
	public String getStarter_name() {
		return starter_name;
	}
	public void setStarter_name(String starter_name) {
		this.starter_name = starter_name;
	}
	public String getDepartment_name() {
		return department_name;
	}
	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}
	public String getTask_name() {
		return task_name;
	}
	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}
	public String getProcessDefinitionIds() {
		return processDefinitionIds;
	}
	public void setProcessDefinitionIds(String processDefinitionIds) {
		this.processDefinitionIds = processDefinitionIds;
	}
	public String getVariable_grasp() {
		return variable_grasp;
	}
	public void setVariable_grasp(String variable_grasp) {
		this.variable_grasp = variable_grasp;
	}

	   
	   
}
