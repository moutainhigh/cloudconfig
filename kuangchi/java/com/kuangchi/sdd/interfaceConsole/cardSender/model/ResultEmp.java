package com.kuangchi.sdd.interfaceConsole.cardSender.model;

import java.util.List;

public class ResultEmp {
	private String msg;// 结果文本信息
	private String code;// 结果代码
	private List<OperateEmpModel> resultList;// 部门结果集

	public List<OperateEmpModel> getResultList() {
		return resultList;
	}

	public void setResultList(List<OperateEmpModel> resultList) {
		this.resultList = resultList;
	}

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

}
