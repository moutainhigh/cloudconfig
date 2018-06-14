package com.kuangchi.sdd.baseConsole.device.model;

import java.util.List;
//获取设备校验时间model
public class TimeResultMsg {
	private String result_code;
	private String result_msg;
	private Time time;//设备校时时间
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
	public Time getTime() {
		return time;
	}
	public void setTime(Time time) {
		this.time = time;
	}
	
	
}
