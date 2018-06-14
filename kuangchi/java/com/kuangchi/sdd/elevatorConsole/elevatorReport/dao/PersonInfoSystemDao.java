package com.kuangchi.sdd.elevatorConsole.elevatorReport.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.elevatorConsole.elevatorReport.model.PersonInfoSystem;

public interface PersonInfoSystemDao {

	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-26 
	 * @功能描述: 获取系统全部人员名单-Dao
	 */
	public List<PersonInfoSystem> getPersonInfoSystem(Map map);
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-26
	 * @功能描述: 获取系统全部人员名单总条数-Dao
	 */
	public Integer getPersonInfoSystemCount(Map map);
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-27
	 * @功能描述: 导出系统人员名单-Dao
	 */
	public List<PersonInfoSystem> exportPersonInfoSystem(Map map);
}
