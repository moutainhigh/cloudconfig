package com.xkd.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task  {


	private String id;
	
	private String taskName;//任务名称
	
	private String taskStatus;//任务状态

	private String taskLevel;//级别

	private String startDate;//开始时间

	private String endDate;//结束时间

	private String taskDetail;//说明

	private String taskUserIds;//参与任务的用户id   ，拼接

	private String taskUserNames;//参与任务的用户name   ，拼接

	private String remind;//提醒时间默认0否则30分钟

	private List<Map<String,String>> taskUserList = new ArrayList<>();//参与任务的用户集合

	private String pcCompanyId;

	private String departmentId;

	private String createdBy;

	private String createDate;

	private String updatedBy;
	
	private String updateDate;

	private String status;

	private String uname;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}



	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getTaskDetail() {
		return taskDetail;
	}

	public void setTaskDetail(String taskDetail) {
		this.taskDetail = taskDetail;
	}

	public String getTaskUserIds() {
		return taskUserIds;
	}

	public void setTaskUserIds(String taskUserIds) {
		this.taskUserIds = taskUserIds;
	}

	public List<Map<String, String>> getTaskUserList() {
		return taskUserList;
	}

	public void setTaskUserList(List<Map<String, String>> taskUserList) {
		this.taskUserList = taskUserList;
	}

	public String getPcCompanyId() {
		return pcCompanyId;
	}

	public void setPcCompanyId(String pcCompanyId) {
		this.pcCompanyId = pcCompanyId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTaskUserNames() {
		return taskUserNames;
	}

	public void setTaskUserNames(String taskUserNames) {
		this.taskUserNames = taskUserNames;
	}

	public String getRemind() {
		return remind;
	}

	public void setRemind(String remind) {
		this.remind = remind;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getTaskLevel() {
		return taskLevel;
	}

	public void setTaskLevel(String taskLevel) {
		this.taskLevel = taskLevel;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}
}
