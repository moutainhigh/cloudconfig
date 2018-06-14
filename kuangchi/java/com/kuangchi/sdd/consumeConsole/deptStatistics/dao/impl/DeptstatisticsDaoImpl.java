package com.kuangchi.sdd.consumeConsole.deptStatistics.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.deptStatistics.dao.IDeptstatisticsDao;
import com.kuangchi.sdd.consumeConsole.deptStatistics.model.DeptStatistics;


@Repository("deptstatisticsDaoImpl")
public class DeptstatisticsDaoImpl extends BaseDaoImpl<DeptStatistics> implements IDeptstatisticsDao {

	@Override
	public String getNameSpace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	

	/**
	 * 查询所有部门消费信息
	 * guibo.chen
	 */
	@Override
	public List<DeptStatistics> selectAllDeptStatistics(
			DeptStatistics dept_record, String Page, String size) {
		int page = Integer.valueOf(Page);
		int rows = Integer.valueOf(size);
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("page", (page-1)*rows);
		mapState.put("rows", rows);
		mapState.put("staff_no", dept_record.getStaff_no());
		mapState.put("dept_num", dept_record.getDept_num());
		mapState.put("begin_time", dept_record.getBegin_time());
		mapState.put("end_time", dept_record.getEnd_time());
		mapState.put("type", dept_record.getType());
		return this.getSqlMapClientTemplate().queryForList("selectAllDeptStatistics", mapState);
	}
	/**
	 * 查询总条数
	 * guibo.chen
	 */
	@Override
	public Integer getAllDeptStatisticsCount(DeptStatistics dept_record) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("staff_no", dept_record.getStaff_no());
		mapState.put("dept_num", dept_record.getDept_num());
		mapState.put("begin_time", dept_record.getBegin_time());
		mapState.put("end_time", dept_record.getEnd_time());
		mapState.put("type", dept_record.getType());
		return queryCount("getAllDeptStatisticsCount",mapState);
	}
	/**
	 * 导出部门消费信息
	 * guibo.chen
	 */
	@Override
	public List<DeptStatistics> exportAllDeptstatistics(
			DeptStatistics dept_record) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("staff_no", dept_record.getStaff_no());
		mapState.put("dept_num", dept_record.getDept_num());
		mapState.put("begin_time", dept_record.getBegin_time());
		mapState.put("end_time", dept_record.getEnd_time());
		mapState.put("type", dept_record.getType());
		return this.getSqlMapClientTemplate().queryForList("exportAllDeptstatistics", mapState);
	}
	
	/**
	 * 查询所有部门编号
	 * guibo.chen
	 */
	@Override
	public List<DeptStatistics> selectByDept() {
		return this.getSqlMapClientTemplate().queryForList("selectByDept", "");
	}
	
	/**
	 * 查询部门收支报表信息
	 * guibo.chen
	 */
	@Override
	public List<DeptStatistics> selectDeptStatistics(
			DeptStatistics dept_record) {
		//int page = Integer.valueOf(Page);
		//int rows = Integer.valueOf(size);,String Page, String size
		Map<String, Object> mapState = new HashMap<String, Object>();
		//mapState.put("page", (page-1)*rows);
		//mapState.put("rows", rows);
		mapState.put("dept_num", dept_record.getDept_num());
		mapState.put("begin_time", dept_record.getBegin_time());
		mapState.put("end_time", dept_record.getEnd_time());
		return this.getSqlMapClientTemplate().queryForList("selectDeptStatistics", mapState);
	}

	@Override
	public Integer selectDeptStatisticsCount(DeptStatistics dept_record) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("dept_num", dept_record.getDept_num());
		mapState.put("begin_time", dept_record.getBegin_time());
		mapState.put("end_time", dept_record.getEnd_time());
		return queryCount("selectDeptStatisticsCount",mapState);
	}
	
	/**
	 * 导出部门信息报表汇总
	 * guibo.chen
	 */
	@Override
	public List<DeptStatistics> ExportDeptStatistics(
			DeptStatistics dept_record) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("dept_num", dept_record.getDept_num());
		mapState.put("begin_time", dept_record.getBegin_time());
		mapState.put("end_time", dept_record.getEnd_time());
		return this.getSqlMapClientTemplate().queryForList("ExportDeptStatistics", mapState);
		
	}
	
	
	
	
}
