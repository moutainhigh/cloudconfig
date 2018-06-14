package com.kuangchi.sdd.baseConsole.count.model;

/**
 * 
 * @创建人　: 肖红丽
 * @创建时间: 2016-4-5下午1:43:37
 * @功能描述:卡片状态bean
 * @参数描述:
 */
public class KcardState {
	private Integer card_state_id;//状态id
	public Integer getCard_state_id() {
		return card_state_id;
	}
	public void setCard_state_id(Integer card_state_id) {
		this.card_state_id = card_state_id;
	}
	private String state_dm;	//状态代码
	private String state_name;  //状态名称
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
}
