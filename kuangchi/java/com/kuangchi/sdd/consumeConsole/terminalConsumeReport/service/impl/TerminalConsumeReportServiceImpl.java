package com.kuangchi.sdd.consumeConsole.terminalConsumeReport.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.consumeConsole.device.model.Device;
import com.kuangchi.sdd.consumeConsole.terminalConsumeReport.dao.TerminalConsumeReportDao;
import com.kuangchi.sdd.consumeConsole.terminalConsumeReport.model.TerminalConsumeModel;
import com.kuangchi.sdd.consumeConsole.terminalConsumeReport.service.TerminalConsumeReportService;



/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-8-25 
 * @功能描述: 终端消费报表-业务实现层
 */
@Transactional
@Service("terminalConsumeReportServiceImpl")
public class TerminalConsumeReportServiceImpl implements TerminalConsumeReportService{

	@Resource(name = "terminalConsumeReportDaoImpl")
	private TerminalConsumeReportDao terminalConsumeReportDao;
	
	@Override
	public List<TerminalConsumeModel> getTerminalConsumeReport(
			Map<String, Object> map) {
		return terminalConsumeReportDao.getTerminalConsumeReport(map);
	}

	@Override
	public Integer getTerminalConsumeReportCount(Map<String, Object> map) {
		return terminalConsumeReportDao.getTerminalConsumeReportCount(map);
	}

	@Override
	public List<TerminalConsumeModel> getTerminalConsumeReportNoLimit(
			Map<String, Object> map) {
		return terminalConsumeReportDao.getTerminalConsumeReportNoLimit(map);
	}


	@Override
	public List<TerminalConsumeModel> countTerminalConsumeReport(
			Map<String, Object> map) {
		return terminalConsumeReportDao.countTerminalConsumeReport(map);
	}

	@Override
	public Integer countTerminalConsumeReportCount(Map<String, Object> map) {
		return terminalConsumeReportDao.countTerminalConsumeReportCount(map);
	}

	@Override
	public List<TerminalConsumeModel> countTerminalConsumeReportNoLimit(
			Map<String, Object> map) {
		return terminalConsumeReportDao.countTerminalConsumeReportNoLimit(map);
	}

	@Override
	public List<Device> getDeviceNum() {
		return terminalConsumeReportDao.getDeviceNum();
	}
}
