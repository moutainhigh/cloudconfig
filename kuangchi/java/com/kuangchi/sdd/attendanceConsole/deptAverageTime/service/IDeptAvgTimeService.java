package com.kuangchi.sdd.attendanceConsole.deptAverageTime.service;

import java.util.List;

import com.kuangchi.sdd.attendanceConsole.deptAverageTime.model.DeptAvgTime;
import com.kuangchi.sdd.base.model.easyui.Grid;



public interface IDeptAvgTimeService {
	
	public Grid<DeptAvgTime> getSearchStatistics(DeptAvgTime deptAvgTime,int page, int rows); //统计部门日均信息
	
	public List<DeptAvgTime> exportSearchStatistics(DeptAvgTime deptAvgTime); //导出部门日均信息
	
	public Grid<DeptAvgTime> getStaffTimeStatistics(DeptAvgTime deptAvgTime, int page, int rows); //统计员工日均信息
	
	public List<DeptAvgTime> exportStaffTimeStatistics(DeptAvgTime deptAvgTime); //统计员工日均信息
	
	public Grid<DeptAvgTime> getAllAvgTimeStatistics(DeptAvgTime deptAvgTime,int page, int rows); //统计所有日均信息
	
	public List<DeptAvgTime> exportAllAvgTimeStatistics(DeptAvgTime deptAvgTime); //导出所有日均信息
	
	public List<DeptAvgTime> getBmdmBySjbmDm(String dept_num);//根据上级代码查询部门代码
	
	public Grid<DeptAvgTime> getAllTwoDeptAvgTime(DeptAvgTime deptAvgTime,int page, int rows);//统计所有二级部门日均信息
	
	public List<DeptAvgTime> exportAllTwoDeptAvgTime(DeptAvgTime deptAvgTime); //导出所有二级部门日均信息
	
	public Grid<DeptAvgTime> getAllStaffAvgTime(DeptAvgTime deptAvgTime,int page, int rows);//统计所有员工日均信息
	
	public List<DeptAvgTime> exportAllStaffAvgTime(DeptAvgTime deptAvgTime);//导出所有员工日均信息
	
}
