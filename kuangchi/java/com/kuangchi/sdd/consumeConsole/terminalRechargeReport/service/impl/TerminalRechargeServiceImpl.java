package com.kuangchi.sdd.consumeConsole.terminalRechargeReport.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.consumeConsole.terminalRechargeReport.dao.TerminalRechargeDao;
import com.kuangchi.sdd.consumeConsole.terminalRechargeReport.model.TerminalRechargeModel;
import com.kuangchi.sdd.consumeConsole.terminalRechargeReport.service.TerminalRechargeService;


/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-8-25 
 * @功能描述: 终端消费报表-业务实现层
 */
@Transactional
@Service("terminalRechargeServiceImpl")
public class TerminalRechargeServiceImpl implements TerminalRechargeService{

	@Resource(name = "terminalRechargeDaoImpl")
	private TerminalRechargeDao terminalRechargeDao;
	
	@Override
	public List<TerminalRechargeModel> getTerminalRechargeReport(
			Map<String, Object> map) {
		return terminalRechargeDao.getTerminalRechargeReport(map);
	}

	@Override
	public Integer getTerminalRechargeReportCount(Map<String, Object> map) {
		return terminalRechargeDao.getTerminalRechargeReportCount(map);
	}

	@Override
	public List<TerminalRechargeModel> getTerminalRechargeReportNoLimit(
			Map<String, Object> map) {
		return terminalRechargeDao.getTerminalRechargeReportNoLimit(map);
	}

	@Override
	public List<TerminalRechargeModel> countTerminalRechargeReport(
			Map<String, Object> map) {
		return terminalRechargeDao.countTerminalRechargeReport(map);
	}

	@Override
	public Integer countTerminalRechargeReportCount(Map<String, Object> map) {
		return terminalRechargeDao.countTerminalRechargeReportCount(map);
	}

	@Override
	public List<TerminalRechargeModel> countTerminalRechargeReportNoLimit(
			Map<String, Object> map) {
		return terminalRechargeDao.countTerminalRechargeReportNoLimit(map);
	}

}
