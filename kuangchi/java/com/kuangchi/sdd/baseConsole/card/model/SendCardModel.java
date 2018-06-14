package com.kuangchi.sdd.baseConsole.card.model;

import java.util.List;

public class SendCardModel {
	private String result_code;//结果码
	private String result_msg;//结果提示信息
	private List<QuerySentcardInfo> card_info;//所有发卡卡片集合
	private String cell_id;
	public String getCell_id() {
		return cell_id;
	}
	public void setCell_id(String cell_id) {
		this.cell_id = cell_id;
	}
	public String getResult_code() {
		return result_code;
	}
	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}
	public String getResult_msg() {
		return result_msg;
	}
	public void setResult_msg(String result_msg) {
		this.result_msg = result_msg;
	}
	public List<QuerySentcardInfo> getCard_info() {
		return card_info;
	}
	public void setCard_info(List<QuerySentcardInfo> card_info) {
		this.card_info = card_info;
	}
	
}
