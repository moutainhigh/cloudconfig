package com.kuangchi.sdd.consumeConsole.deptBonusReport.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.deptBonusReport.dao.DeptBonusReportDao;
import com.kuangchi.sdd.consumeConsole.deptBonusReport.model.DeptBonusReportModel;


/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-8-29 
 * @功能描述: 部门补助报表-dao实现层
 */

@Repository("deptBonusReportDaoImpl")
public class DeptBonusReportDaoImpl extends BaseDaoImpl<Object> implements DeptBonusReportDao{

	/*获取所有部门补助报表信息（分页）*/
	@Override
	public List<DeptBonusReportModel> getDeptBonusReport(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getDeptBonusReport", map);
	}

	/*获取所有部门补助报表信息的总数量*/
	@Override
	public Integer getDeptBonusReportCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getDeptBonusReportCount", map);
	}

	/*获取所有部门补助报表信息(导出用)*/
	@Override
	public List<DeptBonusReportModel> getDeptBonusReportNoLimit(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getDeptBonusReportNoLimit", map);
	}

	/*按照筛选条件统计部门补助报表信息（分页）*/
	@Override
	public List<DeptBonusReportModel> countDeptBonusReport(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("countDeptBonusReport", map);
	}

	/*按照筛选条件统计部门补助报表信息的总数量*/
	@Override
	public Integer countDeptBonusReportCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("countDeptBonusReportCount", map);
	}

	/*按照筛选条件统计部门补助报表信息(导出用)*/
	@Override
	public List<DeptBonusReportModel> countDeptBonusReportNoLimit(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("countDeptBonusReportNoLimit", map);
	}

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

	
}
