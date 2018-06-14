package com.kuangchi.sdd.elevator.model;

import java.util.ArrayList;
import java.util.List;

public class Auth {
    String cardNum;
    String password;
    Integer cardType;
    String startDate;
    String endDate;
    List<Integer> floorList=new ArrayList<Integer>();
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getCardType() {
		return cardType;
	}
	public void setCardType(Integer cardType) {
		this.cardType = cardType;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public List<Integer> getFloorList() {
		return floorList;
	}
	public void setFloorList(List<Integer> floorList) {
		this.floorList = floorList;
	}
    
    
}
