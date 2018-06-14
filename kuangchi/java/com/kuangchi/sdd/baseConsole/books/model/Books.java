package com.kuangchi.sdd.baseConsole.books.model;

import com.kuangchi.sdd.baseConsole.card.model.BaseModelSupport;

public class Books extends BaseModelSupport {
	private Integer wordbook_info_id; //数据字典ID
	private String wordbook_type;//字典类型
	private String wordbook_name;//字典名称
	private String wordbook_value;//字典值
	private String create_time;//创建时间  
	public Integer getWordbook_info_id() {
		return wordbook_info_id;
	}
	public void setWordbook_info_id(Integer wordbook_info_id) {
		this.wordbook_info_id = wordbook_info_id;
	}
	
	public String getWordbook_type() {
		return wordbook_type;
	}
	public void setWordbook_type(String wordbook_type) {
		this.wordbook_type = wordbook_type;
	}
	public String getWordbook_name() {
		return wordbook_name;
	}
	public void setWordbook_name(String wordbook_name) {
		this.wordbook_name = wordbook_name;
	}
	public String getWordbook_value() {
		return wordbook_value;
	}
	public void setWordbook_value(String wordbook_value) {
		this.wordbook_value = wordbook_value;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	
	
	
}
