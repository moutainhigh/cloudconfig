package com.kuangchi.sdd.consumeConsole.terminalConsumeReport.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.device.model.Device;
import com.kuangchi.sdd.consumeConsole.terminalConsumeReport.dao.TerminalConsumeReportDao;
import com.kuangchi.sdd.consumeConsole.terminalConsumeReport.model.TerminalConsumeModel;


/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-8-25 
 * @功能描述: 终端消费报表-dao实现层
 */

@Repository("terminalConsumeReportDaoImpl")
public class TerminalConsumeReportDaoImpl extends BaseDaoImpl<Object> implements TerminalConsumeReportDao{

	
	
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

	
	
	/*获取所有终端消费报表信息（分页）*/
	@Override
	public List<TerminalConsumeModel> getTerminalConsumeReport(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getTerminalConsumeReport", map);
	}

	/*获取所有终端消费报表信息的总数量*/
	@Override
	public Integer getTerminalConsumeReportCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getTerminalConsumeReportCount", map);
	}

	/*获取所有终端消费报表信息(导出用)*/
	@Override
	public List<TerminalConsumeModel> getTerminalConsumeReportNoLimit(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getTerminalConsumeReportNoLimit", map);
	}

	
	/*按照筛选条件统计终端消费报表信息（分页）*/
	@Override
	public List<TerminalConsumeModel> countTerminalConsumeReport(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("countTerminalConsumeReport", map);
	}

	/*按照筛选条件统计终端消费报表信息的总数量*/
	@Override
	public Integer countTerminalConsumeReportCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("countTerminalConsumeReportCount", map);
	}

	/*按照筛选条件统计终端消费报表信息(导出用)*/
	@Override
	public List<TerminalConsumeModel> countTerminalConsumeReportNoLimit(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("countTerminalConsumeReportNoLimit", map);
	}

	/*获取所有设备编号*/
	@Override
	public List<Device> getDeviceNum() {
		return this.getSqlMapClientTemplate().queryForList("getDeviceNum");
	}
 
	
	
	
	
}
