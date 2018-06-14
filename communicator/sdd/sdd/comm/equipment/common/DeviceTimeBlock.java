package com.kuangchi.sdd.comm.equipment.common;

import java.util.List;

public class DeviceTimeBlock {
	private int block;// 时段组块号 00：0-512字节、01：512-1024字节
	private List<DeviceTimeData> deviceTimes;
	public int getBlock() {
		return block;
	}
	public void setBlock(int block) {
		this.block = block;
	}
	
	public List<DeviceTimeData> getDeviceTimes() {
		return deviceTimes;
	}
	public void setDeviceTimes(List<DeviceTimeData> deviceTimes) {
		this.deviceTimes = deviceTimes;
	}
	public long getCrcFromSum() {
		long result = block;
		if (deviceTimes != null) {
			for (DeviceTimeData time : deviceTimes) {
				result+=time.getCrcFromSum();	
			}
		}
		return result;
	}
	
	

}
