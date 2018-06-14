package com.xkd.model;

/**
 * 数据返回唯一对象
 * 返回数据总对象，0000表示成功，其它都表示错误
 * @author fangsj
 *
 */
public class ResponseBP {

	private String repCode = "0000";
	
	private String repNote = "SUCCESS";
	
	private Object resModel;
	
	public ResponseBP(){
		
	}
	
	public ResponseBP(String repCode,String repNote){
		this.repCode = repCode;
		this.repNote = repNote;
	}

	public String getRepCode() {
		return repCode;
	}

	public String getRepNote() {
		return repNote;
	}

	public void setRepNote(String repNote) {
		this.repNote = repNote;
	}

	public void setRepCode(String repCode) {
		this.repCode = repCode;
	}

	public Object getResModel() {
		return resModel;
	}

	public void setResModel(Object resModel) {
		this.resModel = resModel;
	}
}
