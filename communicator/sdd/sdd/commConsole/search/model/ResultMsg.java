package com.kuangchi.sdd.commConsole.search.model;

import java.util.List;

public class ResultMsg {
	private String result_code;
	private String result_msg;
	private List<EquipmentBean> listEquipment;
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
	public List<EquipmentBean> getListEquipment() {
		return listEquipment;
	}
	public void setListEquipment(List<EquipmentBean> listEquipment) {
		this.listEquipment = listEquipment;
	}
	
}
