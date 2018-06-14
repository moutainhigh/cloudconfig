package com.kuangchi.sdd.interfaceConsole.cardSender.model;

public class OprateDpetModel {
	private String bsID;// BS部门的部门代码（唯一，非UUID）
	private String id;// 发卡软件部门id
	private String code;// 部门编号
	private String name;// 部门名称
	private String parentDM;// 父级部门编号
	private String description;// 描述
	private String errorCode;// 错误编号

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBsID() {
		return bsID;
	}

	public void setBsID(String bsID) {
		this.bsID = bsID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getParentDM() {
		return parentDM;
	}

	public void setParentDM(String parentDM) {
		this.parentDM = parentDM;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
