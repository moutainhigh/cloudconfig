package com.kuangchi.sdd.elevatorConsole.elevatorReport.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.model.PersonInfoSystem;

public interface PersonInfoSystemService {
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-26 
	 * @功能描述: 获取系统全部人员名单-Service
	 */
	public Grid<PersonInfoSystem> getPersonInfoSystem(Map map);
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-27
	 * @功能描述: 导出系统人员名单-Service
	 */
	public List<PersonInfoSystem> exportPersonInfoSystem(Map map);
}
