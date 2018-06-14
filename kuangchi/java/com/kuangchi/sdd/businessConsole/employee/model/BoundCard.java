package com.kuangchi.sdd.businessConsole.employee.model;

import com.kuangchi.sdd.baseConsole.card.model.BaseModelSupport;

public class BoundCard extends BaseModelSupport {
	private Integer bc_id;
	private String staff_num;
	private String card_num;
	private String card_type;
	private String card_validity;
	private String bound_date;
	private String unbound_date;
	private String account_num;

	public String getState_name() {
		return state_name;
	}

	public void setState_name(String state_name) {
		this.state_name = state_name;
	}

	private String support_consume;
	private String state_name;

	private String create_time;

	public Integer getBc_id() {
		return bc_id;
	}

	public void setBc_id(Integer bc_id) {
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

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getAccount_num() {
		return account_num;
	}

	public void setAccount_num(String account_num) {
		this.account_num = account_num;
	}

	public String getSupport_consume() {
		return support_consume;
	}

	public void setSupport_consume(String support_consume) {
		this.support_consume = support_consume;
	}

	public String getCard_type() {
		return card_type;
	}

	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}

	public String getCard_validity() {
		return card_validity;
	}

	public void setCard_validity(String card_validity) {
		this.card_validity = card_validity;
	}

}
