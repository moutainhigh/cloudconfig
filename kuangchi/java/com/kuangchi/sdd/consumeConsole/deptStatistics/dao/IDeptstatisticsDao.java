package com.kuangchi.sdd.consumeConsole.deptStatistics.dao;


import java.util.List;

import com.kuangchi.sdd.consumeConsole.deptStatistics.model.DeptStatistics;

public interface IDeptstatisticsDao {
	
	public List<DeptStatistics> selectAllDeptStatistics(DeptStatistics dept_record,String page, String size);//模糊查询部门收支信息
	
	public Integer getAllDeptStatisticsCount(DeptStatistics dept_record);//查询总的行数
	
	public List<DeptStatistics> exportAllDeptstatistics(DeptStatistics dept_record);//导出部门收支信息
	
	public List<DeptStatistics> selectByDept();//查询所有部门编号
	
	public List<DeptStatistics> selectDeptStatistics(DeptStatistics dept_record);//查询部门收支报表信息,String page, String size
	
	public Integer selectDeptStatisticsCount(DeptStatistics dept_record);//查询总的行数
	
	public List<DeptStatistics> ExportDeptStatistics(DeptStatistics dept_record);//导出部门报表汇总
}
