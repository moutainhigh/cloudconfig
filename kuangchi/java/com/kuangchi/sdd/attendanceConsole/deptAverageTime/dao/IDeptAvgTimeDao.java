package com.kuangchi.sdd.attendanceConsole.deptAverageTime.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.attendanceConsole.deptAverageTime.model.DeptAvgTime;



public interface IDeptAvgTimeDao {
	
    	public List<DeptAvgTime> getStaffAndDeptInfo(String dept_num,String staff_no,String staff_name, String layerDeptNum);//查询员工与部门的信息
    	
    	public List<DeptAvgTime> getDateWorkTime(String dept_num,String from_time,String to_time);//查询员工除节假日、公休日外每天的工作时间
    	
    	public List<DeptAvgTime> getDateSumWorkTime(String staff_num,String from_time,String to_time);//查询员工除节假日、公休日外总的工作时间
    	
    	public List<DeptAvgTime> getBmdmBySjbmDm(String sjbm_dm, String layerDeptNum);//根据上级部门代码查询部门编号
    	
    	public List<DeptAvgTime> getStaffDateWorkTime(String staff_num,String from_time,String to_time);
    	
    	public String getDeptNames(String dept_num);//根据部门代码查询部门名称
    	
    	
    	// 查询部门总工时
    	public Map getDeptAvgInfo(Map map);
    	
    	// 查询员工总工时
    	public List<Map> getStaffAvgInfo(Map map);
    	
    	// 查询员工总工时总数
    	public Integer getStaffAvgCount(Map map);
    	
    	// 查询部门员工总数
    	public int getStaffCountByDept(String dept_nums);
    	
    	// 查询部门名称
    	public String getDeptName(String dept_num);
    	
}
