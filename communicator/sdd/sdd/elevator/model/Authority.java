package com.kuangchi.sdd.elevator.model;

import java.util.ArrayList;
import java.util.List;

public class Authority {
    int clear=1;
    int add=1;
    List<Auth>  authList=new ArrayList<Auth>();
	public int getClear() {
		return clear;
	}
	public void setClear(int clear) {
		this.clear = clear;
	}
	public int getAdd() {
		return add;
	}
	public void setAdd(int add) {
		this.add = add;
	}
	public List<Auth> getAuthList() {
		return authList;
	}
	public void setAuthList(List<Auth> authList) {
		this.authList = authList;
	}
	
    
}
