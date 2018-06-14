package com.kuangchi.sdd.businessConsole.activiti.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.form.StartFormHandler;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.SuspensionState;
import org.activiti.engine.impl.task.TaskDefinition;

import com.kuangchi.sdd.base.model.BaseModelSupport;

public class ProcessDefinitionBean   {
		   String id ;
		   String key;
		   String name;
		   int version;
		   String category;
		   String deploymentId;
		   String resourceName;
		   String description;

		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getVersion() {
			return version;
		}
		public void setVersion(int version) {
			this.version = version;
		}
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
		public String getDeploymentId() {
			return deploymentId;
		}
		public void setDeploymentId(String deploymentId) {
			this.deploymentId = deploymentId;
		}
		public String getResourceName() {
			return resourceName;
		}
		public void setResourceName(String resourceName) {
			this.resourceName = resourceName;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}

		   
		   
			
}
