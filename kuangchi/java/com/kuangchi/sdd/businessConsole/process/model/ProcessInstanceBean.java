package com.kuangchi.sdd.businessConsole.process.model;

import com.kuangchi.sdd.base.model.BaseModelSupport;

public class ProcessInstanceBean   extends BaseModelSupport {

	private String id;
	private String name;
	private String startTime;
	private String endTime;
	private Integer duration;
	private String startUserName;
	private String startUserId;
	private String processDefinitionId;
	
	private String currentResponsiblePerson;
	
	
	private String yhDm;
	private String processDefinitionIds;
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public String getStartUserId() {
		return startUserId;
	}
	public void setStartUserId(String startUserId) {
		this.startUserId = startUserId;
	}
	public String getCurrentResponsiblePerson() {
		return currentResponsiblePerson;
	}
	public void setCurrentResponsiblePerson(String currentResponsiblePerson) {
		this.currentResponsiblePerson = currentResponsiblePerson;
	}
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
	public String getYhDm() {
		return yhDm;
	}
	public void setYhDm(String yhDm) {
		this.yhDm = yhDm;
	}
	public String getProcessDefinitionIds() {
		return processDefinitionIds;
	}
	public void setProcessDefinitionIds(String processDefinitionIds) {
		this.processDefinitionIds = processDefinitionIds;
	}


	public String getStartUserName() {
		return startUserName;
	}
	public void setStartUserName(String startUserName) {
		this.startUserName = startUserName;
	}


	
	
	
	
}
