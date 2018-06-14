package com.kuangchi.sdd.commConsole.gateWorkParam.model;

import java.util.List;

import com.kuangchi.sdd.comm.equipment.common.GateParamData;
import com.kuangchi.sdd.comm.equipment.common.GateRecordData;

public class Result_Msg {
	private String result_code;
	private String result_msg;
	private String gateWorkParam;
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
	public String getGateWorkParam() {
		return gateWorkParam;
	}
	public void setGateWorkParam(String gateRecord) {
		this.gateWorkParam = gateRecord;
	}
	
}
