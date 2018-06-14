package com.kuangchi.sdd.elevator.model;

import java.util.ArrayList;
import java.util.List;

public class DelAuth {
    int type;//删除类型  1  删除指定卡号      ，0  删除所有卡号
    List<String> cardNumList=new ArrayList<String>();
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public List<String> getCardNumList() {
		return cardNumList;
	}
	public void setCardNumList(List<String> cardNumList) {
		this.cardNumList = cardNumList;
	}
    
}
