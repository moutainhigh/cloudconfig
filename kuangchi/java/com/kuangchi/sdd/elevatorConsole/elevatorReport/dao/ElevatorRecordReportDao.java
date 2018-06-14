package com.kuangchi.sdd.elevatorConsole.elevatorReport.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.elevatorConsole.elevatorReport.model.ElevatorRecordInfo;

public interface ElevatorRecordReportDao {

	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-26 
	 * @功能描述: 获取电梯刷卡信息报表-Dao
	 */
	public List<ElevatorRecordInfo> getElevatorRecordinfo(Map map);
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-26
	 * @功能描述: 获取电梯刷卡信息总条数-Dao
	 */
	public Integer getElevatorRecordinfoCount(Map map);
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-27
	 * @功能描述: 导出电梯刷卡信息-Dao
	 */
	public List<ElevatorRecordInfo> exportElevatorRecordinfo(Map map);
	
	/**
	 * 最新的电梯刷卡记录
	 * @author minting.he
	 * @return
	 */
	public List<ElevatorRecordInfo> latestElevatorRecord();
	
	/**
	 * 读取后插入事件记录
	 * @author minting.he
	 * @param record
	 * @return
	 */
	public boolean insertEventRecord(Map map);
	
	/**
	 * 根据卡号查员工信息
	 * @author minting.he
	 * @param card_num
	 * @return
	 */
	public HashMap getStaffInfoByCard(String card_num);
	
	/**
	 * 根据楼层编号查名称
	 * @author minting.he
	 * @param map
	 * @return
	 */
	public String getFloorName(Map map);
	
	/**
	 * 获取卡类型
	 * @author minting.he
	 * @param card_num
	 * @return
	 */
	public String getCardTypeName(String card_num);
}
