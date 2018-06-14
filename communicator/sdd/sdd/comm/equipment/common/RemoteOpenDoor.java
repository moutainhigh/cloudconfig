package com.kuangchi.sdd.comm.equipment.common;

import com.kuangchi.sdd.comm.equipment.base.Data;

public class RemoteOpenDoor extends Data {
	private int door;//1 号门  2 二号门  4  三号门  8 四号门
	

	public int getDoor() {
		return door;
	}


	public void setDoor(int door) {
		this.door = door;
	}


	public long getCrcFromSum() {
		return door;
	}	
	
}
