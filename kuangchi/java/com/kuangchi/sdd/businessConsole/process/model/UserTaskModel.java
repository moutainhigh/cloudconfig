package com.kuangchi.sdd.businessConsole.process.model;

import com.kuangchi.sdd.base.model.BaseModelSupport;

public class UserTaskModel  extends BaseModelSupport {
		private String id;
		private String name;
		private String initiator;
		private String startDate;
		private String processDefinitionName;
		private String initiatorDepartment;
		private String delegator;
		private String delegatorDepartment;
		
		private String processDefinitionId;
		private String shouldStaffName;
		private String shouldStaffNum;
		private String shouldStaffDept;
		private Integer lockStatus;
		
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
		
		
		public String getInitiator() {
			return initiator;
		}
		public void setInitiator(String initiator) {
			this.initiator = initiator;
		}
		public String getStartDate() {
			return startDate;
		}
		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}

		public String getProcessDefinitionName() {
			return processDefinitionName;
		}
		public void setProcessDefinitionName(String processDefinitionName) {
			this.processDefinitionName = processDefinitionName;
		}
		public String getInitiatorDepartment() {
			return initiatorDepartment;
		}
		public void setInitiatorDepartment(String initiatorDepartment) {
			this.initiatorDepartment = initiatorDepartment;
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
		public String getProcessDefinitionId() {
			return processDefinitionId;
		}
		public void setProcessDefinitionId(String processDefinitionId) {
			this.processDefinitionId = processDefinitionId;
		}
		public String getDelegator() {
			return delegator;
		}
		public void setDelegator(String delegator) {
			this.delegator = delegator;
		}
		public String getDelegatorDepartment() {
			return delegatorDepartment;
		}
		public void setDelegatorDepartment(String delegatorDepartment) {
			this.delegatorDepartment = delegatorDepartment;
		}
		public String getShouldStaffName() {
			return shouldStaffName;
		}
		public void setShouldStaffName(String shouldStaffName) {
			this.shouldStaffName = shouldStaffName;
		}
		public String getShouldStaffNum() {
			return shouldStaffNum;
		}
		public void setShouldStaffNum(String shouldStaffNum) {
			this.shouldStaffNum = shouldStaffNum;
		}
		public String getShouldStaffDept() {
			return shouldStaffDept;
		}
		public void setShouldStaffDept(String shouldStaffDept) {
			this.shouldStaffDept = shouldStaffDept;
		}
		public Integer getLockStatus() {
			return lockStatus;
		}
		public void setLockStatus(Integer lockStatus) {
			this.lockStatus = lockStatus;
		}

		
		
		
		
		
		
}
