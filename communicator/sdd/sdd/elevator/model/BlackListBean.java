package com.kuangchi.sdd.elevator.model;

import java.util.ArrayList;
import java.util.List;

public class BlackListBean {
     Integer type  ;  //名单类型  0 黑名单 1 白名单
     Integer mode;    //下发模式  1  覆盖  2 查找
     List<String>  list=new ArrayList<String>();
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public List<String> getList() {
		return list;
	}
	public void setList(List<String> list) {
		this.list = list;
	}
	public Integer getMode() {
		return mode;
	}
	public void setMode(Integer mode) {
		this.mode = mode;
	}
     
}
