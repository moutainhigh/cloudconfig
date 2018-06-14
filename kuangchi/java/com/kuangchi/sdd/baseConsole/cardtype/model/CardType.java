package com.kuangchi.sdd.baseConsole.cardtype.model;

import com.kuangchi.sdd.base.model.BaseModelSupport;

 /**
  * @创建人　: 肖红丽
  * @创建时间: 2016-3-30下午6:09:34
  * @功能描述:卡片类型Bean
  * @参数描述:
  */
public class CardType extends BaseModelSupport {
	private Integer card_type_id; // 卡类型ID
	private String type_name; // 类型名称
	private String type_dm; // 类型代码
	private String type_validity; // 类型有效日期
	private String create_time; // 创建时间
	private String validity_flag; // 有效标志
	private String create_user; // 创建人员代码
	private String description; // 描述

	public String getType_validity() {
		return type_validity;
	}

	public void setType_validity(String type_validity) {
		this.type_validity = type_validity;
	}

	public String getType_dm() {
		return type_dm;
	}

	public void setType_dm(String type_dm) {
		this.type_dm = type_dm;
	}

	public Integer getCard_type_id() {
		return card_type_id;
	}

	public void setCard_type_id(Integer card_type_id) {
		this.card_type_id = card_type_id;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getValidity_flag() {
		return validity_flag;
	}

	public void setValidity_flag(String validity_flag) {
		this.validity_flag = validity_flag;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
