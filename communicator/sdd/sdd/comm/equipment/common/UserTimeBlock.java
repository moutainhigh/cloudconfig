package com.kuangchi.sdd.comm.equipment.common;

import java.util.List;

import com.kuangchi.sdd.comm.equipment.base.Data;

public class UserTimeBlock extends Data {
	private int block;// 时段组块号 00：0-512字节、01：512-1024字节
	private List<Integer> times;
	public int getBlock() {
		return block;
	}
	public void setBlock(int block) {
		this.block = block;
	}
	public List<Integer> getTimes() {
		return times;
	}
	public void setTimes(List<Integer> times) {
		this.times = times;
	}
	public long getCrcFromSum() {
		long result = block;
		if (times != null) {
			for (Integer time : times) {
				result+=time;	
			}
		}
		return result;
	}
	
	

}
