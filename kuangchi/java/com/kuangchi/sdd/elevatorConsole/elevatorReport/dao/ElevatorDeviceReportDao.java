package com.kuangchi.sdd.elevatorConsole.elevatorReport.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.elevatorConsole.elevatorReport.model.ElevatorDeviceInfo;

public interface ElevatorDeviceReportDao {

	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-26 
	 * @功能描述: 获取梯控控制设备信息-Dao
	 */
	public List<Map<String, Object>> getElevatorDeviceinfo(Map<String, Object> map);
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-26
	 * @功能描述: 获取梯控控制设备信息总条数-Dao
	 */
	public Integer getElevatorDeviceinfoCount(Map<String, Object> map);
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-27
	 * @功能描述: 导出梯控控制设备信息-Dao
	 */
	public List<Map<String, Object>> exportElevatorDeviceinfo(Map<String, Object> map);
}
