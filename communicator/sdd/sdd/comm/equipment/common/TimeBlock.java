package com.kuangchi.sdd.comm.equipment.common;

import java.util.List;

import com.kuangchi.sdd.comm.equipment.base.Data;

/**
 * 一组时段的集合
 * 
 * @author yu.yao
 * 
 */
public class TimeBlock extends Data {
	private int block;// 时段组块号 00：0-512字节、01：512-1024字节
	private List<TimeGroupData> groups;

	public int getBlock() {
		return block;
	}

	public void setBlock(int block) {
		this.block = block;
	}

	public List<TimeGroupData> getGroups() {
		return groups;
	}

	public void setGroups(List<TimeGroupData> groups) {
		this.groups = groups;
	}

	public long getCrcFromSum() {
		long result = block;
		if (groups != null) {
			for (TimeGroupData group : groups) {
				result += group.getCrcFromSum();
			}
		}
		return result;
	}
}
