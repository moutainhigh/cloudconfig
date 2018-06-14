package com.kuangchi.sdd.consumeConsole.deptRechargeReport.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.deptRechargeReport.dao.DeptRechargeReportDao;
import com.kuangchi.sdd.consumeConsole.deptRechargeReport.model.DeptRechargeReportModel;

/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-8-26 
 * @功能描述: 部门充值报表-dao实现层
 */

@Repository("deptRechargeReportDaoImpl")
public class DeptRechargeReportDaoImpl extends BaseDaoImpl<Object> implements DeptRechargeReportDao{

	
	/*获取所有部门充值报表信息（分页）*/
	@Override
	public List<DeptRechargeReportModel> getDeptRechargeReportByParam(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getDeptRechargeReport", map);
	}

	/*获取所有部门充值报表信息的总数量*/
	@Override
	public Integer getDeptRechargeReportCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getDeptRechargeReportCount", map);
	}

	/*获取所有部门充值报表信息(导出用)*/
	@Override
	public List<DeptRechargeReportModel> getDeptRechargeReportNoLimit(
			Map<String, Object> map) {
		 return this.getSqlMapClientTemplate().queryForList("getDeptRechargeReportNoLimit", map);
	}

	/*按筛选条件获取部门充值报表信息*/
	@Override
	public List<DeptRechargeReportModel> countDeptRechargeReport(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("countDeptRechargeReport", map);
	}

	/*按筛选条件获取部门充值报表信息总数量*/
	@Override
	public Integer countDeptRechargeReportCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("countDeptRechargeReportCount", map);
	}

	/*按筛选条件获取部门充值报表信息(导出用)*/
	@Override
	public List<DeptRechargeReportModel> countDeptRechargeReportNoLimit(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("countDeptRechargeReportNoLimit", map);
	}

	@Override
	public String getNameSpace() {
		return null;
	}

	@Override
	public String getTableName() {
		return null;
	}

}
