package com.kuangchi.sdd.businessConsole.randomCode.model;

public class JsonResult {

	private boolean isSuccess;

	private Object msg;
	
	private String code;
	
	private boolean isNotExist;//用户是否存在
	
	private boolean isOverTime;//用户输入验证码是否超时
	
	private boolean isInvalid;//验证码是否失效

	public boolean isInvalid() {
		return isInvalid;
	}

	public void setInvalid(boolean isInvalid) {
		this.isInvalid = isInvalid;
	}

	public boolean isOverTime() {
		return isOverTime;
	}

	public void setOverTime(boolean isOverTime) {
		this.isOverTime = isOverTime;
	}

	public boolean isNotExist() {
		return isNotExist;
	}

	public void setNotExist(boolean isNotExist) {
		this.isNotExist = isNotExist;
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
