package com.kuangchi.sdd.baseConsole.cardtype.model;

import com.kuangchi.sdd.base.model.BaseModelSupport;

public class ConModel extends BaseModelSupport {
	private String startDate; // 查询条件开始时间
	private String endDate; // 查询条件结束时间
	private String type_name; // 查询条件类型名称
	private String create_user; // 查询条件创建卡片类型的管理员
	private String type_dm; // 类型代码
	private String type_validity; //卡片类型有效期
	
	public String getType_dm() {
		return type_dm;
	}

	public void setType_dm(String type_dm) {
		this.type_dm = type_dm;
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

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getType_validity() {
		return type_validity;
	}

	public void setType_validity(String type_validity) {
		this.type_validity = type_validity;
	}

}
