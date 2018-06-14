package com.kuangchi.sdd.consumeConsole.personBonusReport.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.personBonusReport.dao.PersonBonusReportDao;
import com.kuangchi.sdd.consumeConsole.personBonusReport.model.PersonBonusrReportModel;


/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-8-29 
 * @功能描述: 人员补助报表-dao实现层
 */

@Repository("personBonusReportDaoImpl")
public class PersonBonusReportDaoImpl extends BaseDaoImpl<Object> implements PersonBonusReportDao{

	/*获取所有人员补助报表信息（分页）*/
	@Override
	public List<PersonBonusrReportModel> getPersonBonusReport(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getPersonBonusReport", map);
	}

	/*获取所有人员补助报表信息的总数量*/
	@Override
	public Integer getPersonBonusReportCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getPersonBonusReportCount", map);
	}

	/*获取所有人员补助报表信息(导出用)*/
	@Override
	public List<PersonBonusrReportModel> getPersonBonusReportNoLimit(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getPersonBonusReportNoLimit", map);
	}

	/*按照筛选条件统计人员补助报表信息（分页）*/
	@Override
	public List<PersonBonusrReportModel> countPersonBonusReport(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("countPersonBonusReport", map);
	}

	/*按照筛选条件统计人员补助报表信息的总数量*/
	@Override
	public Integer countPersonBonusReportCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("countPersonBonusReportCount", map);
	}

	/*按照筛选条件统计人员补助报表信息(导出用)*/
	@Override
	public List<PersonBonusrReportModel> countPersonBonusReportNoLimit(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("countPersonBonusReportNoLimit", map);
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
