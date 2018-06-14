package com.kuangchi.sdd.businessConsole.activiti.model;

public class ProcessUserTask {
		String id;
		String name;
		String taskId;
		String assigneeDm;
		String assigneeMc;
		String modelId;
		String isDesignated="0";
		String processDefinitionId;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}

		public String getTaskId() {
			return taskId;
		}
		public void setTaskId(String taskId) {
			this.taskId = taskId;
		}
		public String getAssigneeDm() {
			return assigneeDm;
		}
		public void setAssigneeDm(String assigneeDm) {
			this.assigneeDm = assigneeDm;
		}
		public String getModelId() {
			return modelId;
		}
		public void setModelId(String modelId) {
			this.modelId = modelId;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getIsDesignated() {
			return isDesignated;
		}
		public void setIsDesignated(String isDesignated) {
			this.isDesignated = isDesignated;
		}
		public String getProcessDefinitionId() {
			return processDefinitionId;
		}
		public void setProcessDefinitionId(String processDefinitionId) {
			this.processDefinitionId = processDefinitionId;
		}
		public String getAssigneeMc() {
			return assigneeMc;
		}
		public void setAssigneeMc(String assigneeMc) {
			this.assigneeMc = assigneeMc;
		}
		
		
		
}
