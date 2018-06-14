package com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.model;

import java.util.List;


/**
 * @创建人　: 高育漫
 * @创建时间: 2016-4-5 下午5:12:10
 * @功能描述: 时段信息Bean
 */
public class TimeBlock {
	private int block;// 时段组块号 00：0-512字节、01：512-1024字节
	private List<TimeGroupData> groups;
	private String result_value;
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
	public String getResult_value() {
		return result_value;
	}
	public void setResult_value(String result_value) {
		this.result_value = result_value;
	}
	
	
	
}
