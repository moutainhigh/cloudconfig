package com.kuangchi.sdd.businessConsole.activiti.model;

public class ProcessDef {
		String id;
		String prodefinitionId;
		String name;
		String modelId;
		String deploymentId;
		String isCurrent;
		
		
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
		public String getModelId() {
			return modelId;
		}
		public void setModelId(String modelId) {
			this.modelId = modelId;
		}
		public String getProdefinitionId() {
			return prodefinitionId;
		}
		public void setProdefinitionId(String prodefinitionId) {
			this.prodefinitionId = prodefinitionId;
		}
		public String getDeploymentId() {
			return deploymentId;
		}
		public void setDeploymentId(String deploymentId) {
			this.deploymentId = deploymentId;
		}
		public String getIsCurrent() {
			return isCurrent;
		}
		public void setIsCurrent(String isCurrent) {
			this.isCurrent = isCurrent;
		}
		
		
}
