package com.kuangchi.sdd.consumeConsole.consumeHandle.model;


public class JsonResult {

	private boolean isSuccess;
	private Object msg;
	private String code;
	private Integer alm;  // 00 表示无报警   >0 表示报警
	
	

	public Integer getAlm() {
		return alm;
	}

	public void setAlm(Integer alm) {
		this.alm = alm;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public Object getMsg() {
		return msg;
	}

	public void setMsg(Object msg) {
		this.msg = msg;
	}

}
