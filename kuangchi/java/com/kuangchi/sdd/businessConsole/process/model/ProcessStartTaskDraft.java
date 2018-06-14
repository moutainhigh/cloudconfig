package com.kuangchi.sdd.businessConsole.process.model;

import com.kuangchi.sdd.base.model.BaseModelSupport;

public class ProcessStartTaskDraft  extends BaseModelSupport{
	   String id;
	  String process_definition_id;
	  String process_variables;
	  String staff_num;
	  String update_time;
	  String process_name;
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

	public String getProcess_variables() {
		return process_variables;
	}
	public void setProcess_variables(String process_variables) {
		this.process_variables = process_variables;
	}
	public String getStaff_num() {
		return staff_num;
	}
	public void setStaff_num(String staff_num) {
		this.staff_num = staff_num;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getProcess_name() {
		return process_name;
	}
	public void setProcess_name(String process_name) {
		this.process_name = process_name;
	}
	public String getProcessDefinitionIds() {
		return processDefinitionIds;
	}
	public void setProcessDefinitionIds(String processDefinitionIds) {
		this.processDefinitionIds = processDefinitionIds;
	}  
     
	  
}
