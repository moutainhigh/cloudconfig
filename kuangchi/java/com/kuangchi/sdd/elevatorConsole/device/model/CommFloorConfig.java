package com.kuangchi.sdd.elevatorConsole.device.model;

import java.util.List;

public class CommFloorConfig {

	private Integer type;
	private List<Integer> floorList;
	
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
