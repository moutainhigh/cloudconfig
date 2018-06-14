package com.kuangchi.sdd.consumeConsole.deptStatistics.service;


import java.util.List;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.consumeConsole.deptStatistics.model.DeptStatistics;


public interface IDeptstatisticsService {
	
	public Grid selectAllDeptStatistics(DeptStatistics dept_record,
		String page, String size);//模糊查询部门消费所有信息
	
	public List<DeptStatistics> exportAllDeptstatistics(DeptStatistics dept_record);//导出部门收支信息
	
	public Grid selectDeptStatistics(DeptStatistics dept_record);//查询部门收支报表信息
	
	public List<DeptStatistics> ExportDeptStatistics(DeptStatistics dept_record);//导出部门报表汇总
	
	public List<DeptStatistics> selectByDept();//查询所有部门编号
}
