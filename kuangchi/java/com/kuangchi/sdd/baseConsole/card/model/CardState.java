package com.kuangchi.sdd.baseConsole.card.model;

public class CardState extends BaseModelSupport{
	private String card_state_id;//卡片状态ID
	private String state_name;//状态名称
	private String state_dm;//状态代码
	private String create_time;//创建时间
	public String getCard_state_id() {
		return card_state_id;
	}
	public void setCard_state_id(String card_state_id) {
		this.card_state_id = card_state_id;
	}
	public String getState_name() {
		return state_name;
	}
	public void setState_name(String state_name) {
		this.state_name = state_name;
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
