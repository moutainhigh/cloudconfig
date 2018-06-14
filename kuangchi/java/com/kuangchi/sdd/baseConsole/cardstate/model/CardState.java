package com.kuangchi.sdd.baseConsole.cardstate.model;



public class CardState{
	private Integer card_state_id;//卡片状态ID
	private String state_name;//状态名称
	private String state_validity;//有效期
	private String state_dm;//状态代码
	private String create_time;//创建时间
	private String validity_flag;//有效标志
	private String create_user;//创建人员
	private String description;//描述
	private String begin_time;//开始时间
	private String end_time;//结束时间
	private Integer editable;
	
	
	public Integer getEditable() {
		return editable;
	}
	public void setEditable(Integer editable) {
		this.editable = editable;
	}
	public String getBegin_time() {
		return begin_time;
	}
	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public Integer getCard_state_id() {
		return card_state_id;
	}
	public void setCard_state_id(Integer card_state_id) {
		this.card_state_id = card_state_id;
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
	public String getState_name() {
		return state_name;
	}
	public void setState_name(String state_name) {
		this.state_name = state_name;
	}
	public String getState_validity() {
		return state_validity;
	}
	public void setState_validity(String state_validity) {
		this.state_validity = state_validity;
	}
	public String getState_dm() {
		return state_dm;
	}
	public void setState_dm(String state_dm) {
		this.state_dm = state_dm;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	
	
	


	
	
}
