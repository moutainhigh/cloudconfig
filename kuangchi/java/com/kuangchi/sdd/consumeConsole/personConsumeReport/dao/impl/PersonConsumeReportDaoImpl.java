package com.kuangchi.sdd.consumeConsole.personConsumeReport.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.personConsumeReport.dao.PersonConsumeReportDao;
import com.kuangchi.sdd.consumeConsole.personConsumeReport.model.PersonConsumeReportModel;


/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-8-24 
 * @功能描述: 人员消费报表-dao实现层
 */

@Repository("personConsumeReportDaoImpl")
public class PersonConsumeReportDaoImpl extends BaseDaoImpl<Object> implements PersonConsumeReportDao{
    
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
	
	/*获取所有人员消费报表信息（分页）*/
	@Override
	public List<PersonConsumeReportModel> getPersonConsumeReport(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getPersonConsumeReport", map);
	}
    
	/*获取所有人员消费报表信息的总数量*/
	@Override
	public Integer getPersonConsumeReportCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getPersonConsumeReportCount", map);
	}

	/*获取所有人员消费报表信息(导出用)*/
	@Override
	public List<PersonConsumeReportModel> getPersonConsumeReportNoLimit(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getPersonConsumeReportNoLimit", map);
	}
    
	
	/*按照筛选条件统计人员消费报表信息（分页）*/
	@Override
	public List<PersonConsumeReportModel> countPersonConsumeReport(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("countPersonConsumeReport", map);
	}
    
	/*按照筛选条件统计人员消费报表信息的总数量*/
	@Override
	public Integer countPersonConsumeReportCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("countPersonConsumeReportCount", map);
	}

	/*按照筛选条件统计人员消费报表信息(导出用)*/
	@Override
	public List<PersonConsumeReportModel> countPersonConsumeReportNoLimit(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("countPersonConsumeReportNoLimit", map);
	}

	

}
