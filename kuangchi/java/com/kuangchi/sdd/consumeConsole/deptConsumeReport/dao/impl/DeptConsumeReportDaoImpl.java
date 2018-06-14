package com.kuangchi.sdd.consumeConsole.deptConsumeReport.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.deptConsumeReport.dao.DeptConsumeReportDao;
import com.kuangchi.sdd.consumeConsole.deptConsumeReport.model.DeptConsumeReportModel;
import com.kuangchi.sdd.consumeConsole.vendor.model.Vendor;

/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-8-19 
 * @功能描述: 部门消费报表-dao实现层
 */

@Repository("deptConsumeReportDaoImpl")
public class DeptConsumeReportDaoImpl extends BaseDaoImpl<Object> implements DeptConsumeReportDao{


	@Override
	public String getNameSpace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		return null;
	}
	
	/*获取所有部门消费报表信息（分页）*/
	@Override
	public List<DeptConsumeReportModel> getDeptConsumeReportByParam(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getDeptConsumeReportByParam", map);
	}
   
	/*获取所有部门消费报表信息的总数量*/
	@Override
	public Integer getDeptConsumeReportCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getDeptConsumeReportCount", map);
	}
    
	/*按筛选条件获取部门消费报表信息*/
	@Override
	public List<DeptConsumeReportModel> initDeptConsumeReport(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("initDeptConsumeReport", map);
	}
    
	/*按筛选条件获取部门消费报表信息总数量*/
	@Override
	public Integer initDeptConsumeReportCount(
			Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("initDeptConsumeReportCount", map);
	}
    
	/*获取所有部门消费报表信息(导出用)*/
	@Override
	public List<DeptConsumeReportModel> getDeptConsumeReportNoLimit(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getDeptConsumeReportNoLimit", map);
	}

	/*按筛选条件获取部门消费报表信息(导出用)*/
	@Override
	public List<DeptConsumeReportModel> initDeptConsumeReportNoLimit(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("initDeptConsumeReportNoLimit", map);
	}
	/*获取所有商户类型*/
	@Override
	public List<Vendor> getVendor() {
		return this.getSqlMapClientTemplate().queryForList("getVendor");
	}

	

	
}
