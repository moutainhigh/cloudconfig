package com.kuangchi.sdd.commConsole.status.model;

import java.util.List;

public class ResultMsg {
	private String result_code;
	private String result_msg;
	private List<GateParamData> allStatus;

	public List<GateParamData> getAllStatus() {
		return allStatus;
	}
	public void setAllStatus(List<GateParamData> allStatus) {
		this.allStatus = allStatus;
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
}
