package com.kuangchi.sdd.elevator.model;

public class Result {
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
