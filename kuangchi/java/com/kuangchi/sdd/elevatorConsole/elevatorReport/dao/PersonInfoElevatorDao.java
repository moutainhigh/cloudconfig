package com.kuangchi.sdd.elevatorConsole.elevatorReport.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.elevatorConsole.elevatorReport.model.PersonInfoElevator;

public interface PersonInfoElevatorDao {

	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-26 
	 * @功能描述: 获取梯控人员名单-Dao
	 */
	public List<PersonInfoElevator> getPersonInfoElevator(Map map);
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-26
	 * @功能描述: 获取梯控人员名单总条数-Dao
	 */
	public Integer getPersonInfoElevatorCount(Map map);
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-27
	 * @功能描述: 导出梯控人员名单-Dao
	 */
	public List<PersonInfoElevator> exportPersonInfoElevator(Map map);
}
