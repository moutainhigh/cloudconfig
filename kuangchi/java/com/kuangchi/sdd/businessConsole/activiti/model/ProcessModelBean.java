package com.kuangchi.sdd.businessConsole.activiti.model;

import org.apache.commons.net.ntp.TimeStamp;

import com.kuangchi.sdd.base.model.BaseModelSupport;

public class ProcessModelBean   {
		private String id;
		private String name;
		private String key;
		private String createTime;
		private String updateTime;
		private String metaInfo;
		private String description;
		private int version;
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
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getCreateTime() {
			return createTime;
		}
		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}
		public String getUpdateTime() {
			return updateTime;
		}
		public void setUpdateTime(String updateTime) {
			this.updateTime = updateTime;
		}
		public String getMetaInfo() {
			return metaInfo;
		}
		public void setMetaInfo(String metaInfo) {
			this.metaInfo = metaInfo;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public int getVersion() {
			return version;
		}
		public void setVersion(int version) {
			this.version = version;
		}
		
		
		
		
		
		
		
}
