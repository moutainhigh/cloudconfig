package com.kuangchi.sdd.interfaceConsole.cardSender.model;

import com.kuangchi.sdd.base.model.BaseModelSupport;

public class CardSyncModel extends BaseModelSupport {

	private String bc_id;// 人卡绑定id
	private String staff_num;// 员工编号（UUID）
	private String card_num;// 卡号
	private String bound_date;// 绑定日期
	private String unbound_date;// 解绑日期
	private String support_consume;// 是否支持消费
	private String create_user;// 创建人
	private String create_time;// 发卡时间

	private String type_dm;// 卡片类型代码
	private String state_dm;// 卡片状态代码
	private String card_validity;// 有效期

	public String getType_dm() {
		return type_dm;
	}

	public void setType_dm(String type_dm) {
		this.type_dm = type_dm;
	}

	public String getState_dm() {
		return state_dm;
	}

	public void setState_dm(String state_dm) {
		this.state_dm = state_dm;
	}

	public String getCard_validity() {
		return card_validity;
	}

	public void setCard_validity(String card_validity) {
		this.card_validity = card_validity;
	}

	public String getBc_id() {
		return bc_id;
	}

	public void setBc_id(String bc_id) {
		this.bc_id = bc_id;
	}

	public String getStaff_num() {
		return staff_num;
	}

	public void setStaff_num(String staff_num) {
		this.staff_num = staff_num;
	}

	public String getCard_num() {
		return card_num;
	}

	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}

	public String getBound_date() {
		return bound_date;
	}

	public void setBound_date(String bound_date) {
		this.bound_date = bound_date;
	}

	public String getUnbound_date() {
		return unbound_date;
	}

	public void setUnbound_date(String unbound_date) {
		this.unbound_date = unbound_date;
	}

	public String getSupport_consume() {
		return support_consume;
	}

	public void setSupport_consume(String support_consume) {
		this.support_consume = support_consume;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

}
