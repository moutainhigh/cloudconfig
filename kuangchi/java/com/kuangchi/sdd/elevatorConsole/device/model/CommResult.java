package com.kuangchi.sdd.elevatorConsole.device.model;

public class CommResult {
	private Integer resultCode;
    private String resultTxt;
	public Integer getResultCode() {
		return resultCode;
	}
	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultTxt() {
		return resultTxt;
	}
	public void setResultTxt(String resultTxt) {
		this.resultTxt = resultTxt;
	}

	@Override
	public String toString() {

		return "{\"resultCode\":"+resultCode+",\"resultTxt\":\""+resultTxt+"\"}";
	}
}