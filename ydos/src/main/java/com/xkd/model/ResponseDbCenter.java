package com.xkd.model;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 数据返回唯一对象
 * 返回数据总对象，0000表示成功，其它都表示错误
 * @author fangsj
 *
 */
@ApiModel
public class ResponseDbCenter {

	private String repCode = "S0000";
	
	private String repNote = "SUCCESS";
	
	private String pertain = "CRM";

	//因为前段需要统计页数，加上这个属性
	private String totalRows = "0";
	
	private Object resModel;
	
	private Object resExtra;
	
	public ResponseDbCenter() {
	}

	public ResponseDbCenter(String repCode,String repNote){
		this.repCode = repCode;
		this.repNote = repNote;
		this.pertain = "CRM";
	}
	
	public ResponseDbCenter(String repCode,String repNote, String pertain){
		this.repCode = repCode;
		this.repNote = repNote;
		this.pertain = pertain;
	}

	@ApiModelProperty(value = "返回代码",required = true, example = "")
	public String getRepCode() {
		return repCode;
	}

	@ApiModelProperty(value = "返回描述",required = true, example = "")
	public String getRepNote() {
		return repNote;
	}

	public void setRepNote(String repNote) {
		this.repNote = repNote;
	}

	public void setRepCode(String repCode) {
		this.repCode = repCode;
	}

	@ApiModelProperty(value = "返回数据",required = true, example = "三二麻子")
	public Object getResModel() {
		return resModel;
	}

	public void setResModel(Object resModel) {
		this.resModel = resModel;
	}

	@ApiModelProperty(value = "属于哪个模块",required = true, example = "")
	public String getPertain() {
		return pertain;
	}

	public void setPertain(String pertain) {
		this.pertain = pertain;
	}

	@ApiModelProperty(value = "总条数",required = true, example = "")
	public String getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(String totalRows) {
		this.totalRows = totalRows;
	}

	@ApiModelProperty(value = "其它信息",required = true, example = "")
	public Object getResExtra() {
		return resExtra;
	}

	public void setResExtra(Object resExtra) {
		this.resExtra = resExtra;
	}
	
	
	
	
}
