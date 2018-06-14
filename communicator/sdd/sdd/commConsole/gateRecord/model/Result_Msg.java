package com.kuangchi.sdd.commConsole.gateRecord.model;

import java.util.HashMap;
import java.util.List;

import com.kuangchi.sdd.comm.equipment.common.GateRecordData;

public class Result_Msg {
	private String result_code;
	private String result_msg;
	private List<HashMap> gateRecordList;
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
	public List<HashMap> getGateRecordList() {
		return gateRecordList;
	}
	public void setGateRecordList(List<HashMap> gateRecordList) {
		this.gateRecordList = gateRecordList;
	}
	
}
