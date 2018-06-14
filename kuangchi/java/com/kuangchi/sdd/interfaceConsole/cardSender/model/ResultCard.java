package com.kuangchi.sdd.interfaceConsole.cardSender.model;

import java.util.List;

public class ResultCard {
	private String msg;// 结果文本信息
	private String code;// 结果代码
	private List<CardOperaSyncModel> resultList;// 部门结果集

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<CardOperaSyncModel> getResultList() {
		return resultList;
	}

	public void setResultList(List<CardOperaSyncModel> resultList) {
		this.resultList = resultList;
	}

}
