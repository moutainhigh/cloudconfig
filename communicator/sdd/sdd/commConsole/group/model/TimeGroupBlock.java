package com.kuangchi.sdd.commConsole.group.model;

import org.apache.struts2.json.annotations.JSON;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kuangchi.sdd.comm.equipment.common.TimeData;

public class TimeGroupBlock {
	private TimeData start;//起始时间
    private TimeData end;//起始时间
	private String block;
	private JsonArray groups;

	public JsonArray getGroups() {
		return groups;
	}
	public void setGroups(JsonArray groups) {
		this.groups = groups;
	}
	public TimeData getStart() {
		return start;
	}
	public void setStart(TimeData start) {
		this.start = start;
	}
	public TimeData getEnd() {
		return end;
	}
	public void setEnd(TimeData end) {
		this.end = end;
	}
	public String getBlock() {
		return block;
	}
	public void setBlock(String block) {
		this.block = block;
	}
	
	
	
}
