package com.kuangchi.sdd.attendanceConsole.deptAverageTime.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.Query;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.deptAverageTime.dao.IDeptAvgTimeDao;
import com.kuangchi.sdd.attendanceConsole.deptAverageTime.model.DeptAvgTime;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;

@Repository("deptAvgTimeDaoImpl")
public class DeptAvgTimeDaoImpl extends BaseDaoImpl<Object> implements IDeptAvgTimeDao {

	
	@Override
	public String getNameSpace() {
		// TODO Auto-generated method stub
		return "common.deptAvgTime";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 查询员工与部门的信息-guibo.chen
	 * @param dept_num
	 * @return
	 */
	@Override
	public List<DeptAvgTime> getStaffAndDeptInfo(String dept_num,String staff_no,String staff_name, String layerDeptNum) {
		Map<String,Object> map= new HashMap<String,Object>();
		map.put("dept_num", dept_num);
		map.put("staff_no", staff_no);
		map.put("staff_name", staff_name);
		map.put("layerDeptNum", layerDeptNum);
		List<DeptAvgTime> dept=getSqlMapClientTemplate().queryForList("getStaffAndDeptInfo", map);
		return dept;
	}
	

	
	/**
	 * 查询员工除节假日、公休日外每天的工作时间天数-guibo.chen
	 * @param dept_num
	 * @return
	 */
	@Override
	public List<DeptAvgTime> getDateWorkTime(String dept_num,String from_time,String to_time) {
		Map<String,Object> map= new HashMap<String,Object>();
		map.put("dept_num", dept_num);
		map.put("from_time", from_time);
		map.put("to_time", to_time);
		return getSqlMapClientTemplate().queryForList("getDateWorkTime", map);
	}
	/**
	 * 查询员工除节假日、公休日外总的工作时间-guibo.chen
	 * @param dept_num
	 * @return
	 */
	@Override
	public List<DeptAvgTime> getDateSumWorkTime(String staff_num,String from_time,String to_time) {
		Map<String,Object> map= new HashMap<String,Object>();
		map.put("staff_num", staff_num);
		map.put("from_time", from_time);
		map.put("to_time", to_time);
		return getSqlMapClientTemplate().queryForList("getDateSumWorkTime", map);
	}

	@Override
	public List<DeptAvgTime> getBmdmBySjbmDm(String sjbm_dm, String layerDeptNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sjbm_dm", sjbm_dm);
		map.put("layerDeptNum", layerDeptNum);
		return getSqlMapClientTemplate().queryForList("getBmdmBySjbmDm", map);
	}
	
	//根据员工编号查询实际工作天数
	@Override
	public List<DeptAvgTime> getStaffDateWorkTime(String staff_num,
			String from_time, String to_time) {
		Map<String,Object> map= new HashMap<String,Object>();
		map.put("staff_num", staff_num);
		map.put("from_time", from_time);
		map.put("to_time", to_time);
		return getSqlMapClientTemplate().queryForList("getStaffDateWorkTime", map);
	}

	@Override
	public String getDeptNames(String dept_num) {
		return (String) getSqlMapClientTemplate().queryForObject("getDeptNames", dept_num);
	}

	@Override
	public Map getDeptAvgInfo(Map map) {
		return (Map) getSqlMapClientTemplate().queryForObject("getDeptAvgInfo", map);
	}
	
	@Override
	public List<Map> getStaffAvgInfo(Map map) {
		return getSqlMapClientTemplate().queryForList("getStaffAvgInfo", map);
	}
	
	@Override
	public Integer getStaffAvgCount(Map map) {
		return (Integer)getSqlMapClientTemplate().queryForObject("getStaffAvgCount", map);
	}

	@Override
	public int getStaffCountByDept(String dept_nums) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getStaffCountByDept", dept_nums);
	}
	
	@Override
	public String getDeptName(String dept_num) {
		return (String) getSqlMapClientTemplate().queryForObject("getDeptName", dept_num);
	}

	


   
}
