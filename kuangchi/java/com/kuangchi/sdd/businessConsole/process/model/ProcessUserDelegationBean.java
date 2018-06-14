package com.kuangchi.sdd.businessConsole.process.model;

import com.kuangchi.sdd.base.model.BaseModelSupport;

public class ProcessUserDelegationBean extends BaseModelSupport  {
    private String id;
	  private String model_Id;
	  private String staff_num;
	  private String delegator;
	  private String delegator_name;
	  private String model_name;
	  
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getModel_Id() {
		return model_Id;
	}
	public void setModel_Id(String model_Id) {
		this.model_Id = model_Id;
	}
	public String getStaff_num() {
		return staff_num;
	}
	public void setStaff_num(String staff_num) {
		this.staff_num = staff_num;
	}
	public String getDelegator() {
		return delegator;
	}
	public void setDelegator(String delegator) {
		this.delegator = delegator;
	}
	public String getDelegator_name() {
		return delegator_name;
	}
	public void setDelegator_name(String delegator_name) {
		this.delegator_name = delegator_name;
	}
	public String getModel_name() {
		return model_name;
	}
	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}
	  
	  
}