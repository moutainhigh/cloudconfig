package com.kuangchi.sdd.commConsole.gateLimit.model;

import java.util.List;

import com.kuangchi.sdd.comm.equipment.common.GateLimitData;

public class ResultMsg {
	private String result_code;
	private String result_msg;
	private List<GateLimitData> gateLimitList;
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
	public List<GateLimitData> getGateLimitList() {
		return gateLimitList;
	}
	public void setGateLimitList(List<GateLimitData> gateLimitList) {
		this.gateLimitList = gateLimitList;
	}
	
	
}
