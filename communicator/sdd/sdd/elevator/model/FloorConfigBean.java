package com.kuangchi.sdd.elevator.model;

import java.util.ArrayList;
import java.util.List;

public class FloorConfigBean {
     Integer type;
     List<Integer> floorList=new ArrayList<Integer>();
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public List<Integer> getFloorList() {
		return floorList;
	}
	public void setFloorList(List<Integer> floorList) {
		this.floorList = floorList;
	}
     
     
}
