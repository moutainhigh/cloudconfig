package com.kuangchi.sdd.baseConsole.card.model;

public class StaffCard extends BaseModelSupport {
	private String staff_no;
	private String staff_name;
	private String card_id;// 卡片ID
	private String card_num;// 卡号
	private String card_pledge;// 卡片押金
	private String card_type_id;// 类型名称ID
	private String card_validity;// 卡片有效期
	private String state_dm;// 卡状态码
	private String state_name;// 卡状态名称
	private String card_validity_state;// 卡片有效状态
	private String create_time;// 发卡时间

	public String getStaff_no() {
		return staff_no;
	}

	public void setStaff_no(String staff_no) {
		this.staff_no = staff_no;
	}

	public String getStaff_name() {
		return staff_name;
	}

	public void setStaff_name(String staff_name) {
		this.staff_name = staff_name;
	}

	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

	public String getCard_num() {
		return card_num;
	}

	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}

	public String getCard_pledge() {
		return card_pledge;
	}

	public void setCard_pledge(String card_pledge) {
		this.card_pledge = card_pledge;
	}

	public String getCard_type_id() {
		return card_type_id;
	}

	public void setCard_type_id(String card_type_id) {
		this.card_type_id = card_type_id;
	}

	public String getCard_validity() {
		return card_validity;
	}

	public void setCard_validity(String card_validity) {
		this.card_validity = card_validity;
	}

	public String getState_dm() {
		return state_dm;
	}

	public void setState_dm(String state_dm) {
		this.state_dm = state_dm;
	}

	public String getState_name() {
		return state_name;
	}

	public void setState_name(String state_name) {
		this.state_name = state_name;
	}

	public String getCard_validity_state() {
		return card_validity_state;
	}

	public void setCard_validity_state(String card_validity_state) {
		this.card_validity_state = card_validity_state;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

}
