package com.kuangchi.sdd.elevatorConsole.elevatorReport.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.model.ElevatorRecordInfo;

public interface ElevatorRecordReportService {
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-26 
	 * @功能描述: 获取电梯刷卡信息报表-Service
	 */
	public Grid<ElevatorRecordInfo> getElevatorRecordinfo(Map map);
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-27
	 * @功能描述: 导出电梯刷卡信息-Service
	 */
	public List<ElevatorRecordInfo> exportElevatorRecordinfo(Map map);
	
	/**
	 * 最新的电梯刷卡记录
	 * @author minting.he
	 * @return
	 */
	public List<ElevatorRecordInfo> latestElevatorRecord();
}
