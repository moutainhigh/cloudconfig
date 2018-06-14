package com.kuangchi.sdd.consumeConsole.personRechargeReport.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.personRechargeReport.dao.PersonRechargeReportDao;
import com.kuangchi.sdd.consumeConsole.personRechargeReport.model.PersonRechargeReportModel;


/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-8-26 
 * @功能描述: 人员充值报表-dao实现层
 */

@Repository("personRechargeReportDaoImpl")
public class PersonRechargeReportDaoImpl extends BaseDaoImpl<Object> implements PersonRechargeReportDao{

	
	/*获取所有人员充值报表信息（分页）*/
	@Override
	public List<PersonRechargeReportModel> getPersonRechargeReport(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getPersonRechargeReport", map);
	}
    
	/*获取所有人员充值报表信息的总数量*/
	@Override
	public Integer getPersonRechargeReportCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getPersonRechargeReportCount", map);
	}

	/*获取所有人员充值报表信息(导出用)*/
	@Override
	public List<PersonRechargeReportModel> getPersonRechargeReportNoLimit(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getPersonRechargeReportNoLimit", map);
	}

	

	/*按照筛选条件统计人员充值报表信息（分页）*/
	@Override
	public List<PersonRechargeReportModel> countPersonRechargeReport(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("countPersonRechargeReport", map);
	}

	/*按照筛选条件统计人员充值报表信息的总数量*/
	@Override
	public Integer countPersonRechargeReportCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("countPersonRechargeReportCount", map);
	}

	/*按照筛选条件统计人员充值报表信息(导出用)*/
	@Override
	public List<PersonRechargeReportModel> countPersonRechargeReportNoLimit(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("countPersonRechargeReportNoLimit", map);
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
