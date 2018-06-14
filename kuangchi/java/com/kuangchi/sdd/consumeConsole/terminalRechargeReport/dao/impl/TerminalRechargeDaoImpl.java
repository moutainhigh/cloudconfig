package com.kuangchi.sdd.consumeConsole.terminalRechargeReport.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.terminalRechargeReport.dao.TerminalRechargeDao;
import com.kuangchi.sdd.consumeConsole.terminalRechargeReport.model.TerminalRechargeModel;

/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-8-26 
 * @功能描述: 终端充值报表-dao实现层
 */

@Repository("terminalRechargeDaoImpl")
public class TerminalRechargeDaoImpl extends BaseDaoImpl<Object> implements TerminalRechargeDao{

	/*获取所有终端充值报表信息（分页）*/
	@Override
	public List<TerminalRechargeModel> getTerminalRechargeReport(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getTerminalRechargeReport", map);
	}

	/*获取所有终端充值报表信息的总数量*/
	@Override
	public Integer getTerminalRechargeReportCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getTerminalRechargeReportCount", map);
	}

	/*获取所有终端充值报表信息(导出用)*/
	@Override
	public List<TerminalRechargeModel> getTerminalRechargeReportNoLimit(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getTerminalRechargeReportNoLimit", map);
	}

	/*按照筛选条件统计终端充值报表信息（分页）*/
	@Override
	public List<TerminalRechargeModel> countTerminalRechargeReport(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("countTerminalRechargeReport", map);
	}

	/*按照筛选条件统计终端充值报表信息的总数量*/
	@Override
	public Integer countTerminalRechargeReportCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("countTerminalRechargeReportCount", map);
	}

	/*按照筛选条件统计终端充值报表信息(导出用)*/
	@Override
	public List<TerminalRechargeModel> countTerminalRechargeReportNoLimit(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("countTerminalRechargeReportNoLimit", map);
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
