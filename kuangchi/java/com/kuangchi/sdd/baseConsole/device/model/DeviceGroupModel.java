package com.kuangchi.sdd.baseConsole.device.model;

public class DeviceGroupModel {
       private Integer groupId;  //设备组ID
       private String  groupNum; //设备组编号
       private String  groupName; //设备组名称
       private String  parentGroupNum; //上级设备组编号
       private String  validityFlag; //有效标志
       private String  createUser; //创建人员代码
       private String  createTime; //创建时间
       private String  description; //描述
       private String flag;
	
    
    public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public String getGroupNum() {
		return groupNum;
	}
	public void setGroupNum(String groupNum) {
		this.groupNum = groupNum;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getParentGroupNum() {
		return parentGroupNum;
	}
	public void setParentGroupNum(String parentGroupNum) {
		this.parentGroupNum = parentGroupNum;
	}
	public String getValidityFlag() {
		return validityFlag;
	}
	public void setValidityFlag(String validityFlag) {
		this.validityFlag = validityFlag;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
}
