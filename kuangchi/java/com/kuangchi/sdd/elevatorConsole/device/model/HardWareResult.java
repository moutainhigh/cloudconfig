package com.kuangchi.sdd.elevatorConsole.device.model;

public class HardWareResult {
	private HardWareParam hardWareParam;
	private boolean isSuccess;

	private Object msg;
	
	private String code;

	public HardWareParam getHardWareParam() {
		return hardWareParam;
	}

	public void setHardWareParam(HardWareParam hardWareParam) {
		this.hardWareParam = hardWareParam;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}