package com.kuangchi.sdd.consum.bean;

public class ResultMsg {
	private String result_code;
	private String result_msg;
	private ParameterUpResponse parameterUpResponse;

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public ParameterUpResponse getParameterUpResponse() {
		return parameterUpResponse;
	}

	public void setParameterUpResponse(ParameterUpResponse parameterUpResponse) {
		this.parameterUpResponse = parameterUpResponse;
	}

	public String getResult_msg() {
		return result_msg;
	}

	public void setResult_msg(String result_msg) {
		this.result_msg = result_msg;
	}

}
