package com.kuangchi.sdd.util.excel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TitleRowCell {
	
	private boolean isRequire;//是否必填
	
	private String titleName;//列名称
	private List<String> suggest;//下拉提示列表项
	
	/**
	 * 列模板
	 * @param titleName 列名
	 * @param isRequire 是否必填
	 */
	public TitleRowCell(String titleName,boolean isRequire) {
		
		this.titleName = titleName;
		this.isRequire = isRequire;
		
		suggest = new ArrayList<String>();
	}

	public String getTitleName() {
		return titleName;
	}
	
	
	public void addSuggest(String sug){
		this.suggest.add(sug);
	}
	
	public void addSuggestS(Collection<String> sugs){
		for(String tmp : sugs){
			addSuggest(tmp);
		}
	}
	
	

	public List<String> getSuggest() {
		return Collections.unmodifiableList(suggest);
	}

	public boolean isRequire() {
		return isRequire;
	}
	

}
