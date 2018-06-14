package com.kuangchi.sdd.consumeConsole.personConsumeReport.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.consumeConsole.personConsumeReport.dao.PersonConsumeReportDao;
import com.kuangchi.sdd.consumeConsole.personConsumeReport.model.PersonConsumeReportModel;
import com.kuangchi.sdd.consumeConsole.personConsumeReport.service.PersonConsumeReportService;

/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-8-24 
 * @功能描述: 人员消费报表-业务实现层
 */
@Transactional
@Service("personConsumeReportServiceImpl")
public class PersonConsumeReportServiceImpl implements PersonConsumeReportService{
    
	@Resource(name = "personConsumeReportDaoImpl")
	private PersonConsumeReportDao personConsumeReportDao;
	
	@Override
	public List<PersonConsumeReportModel> getPersonConsumeReport(
			Map<String, Object> map) {
		return personConsumeReportDao.getPersonConsumeReport(map);
	}

	@Override
	public Integer getPersonConsumeReportCount(Map<String, Object> map) {
		return personConsumeReportDao.getPersonConsumeReportCount(map);
	}

	@Override
	public List<PersonConsumeReportModel> getPersonConsumeReportNoLimit(
			Map<String, Object> map) {
		return personConsumeReportDao.getPersonConsumeReportNoLimit(map);
	}

	@Override
	public List<PersonConsumeReportModel> countPersonConsumeReport(
			Map<String, Object> map) {
		return personConsumeReportDao.countPersonConsumeReport(map);
	}

	@Override
	public Integer countPersonConsumeReportCount(Map<String, Object> map) {
		return personConsumeReportDao.countPersonConsumeReportCount(map);
	}

	@Override
	public List<PersonConsumeReportModel> countPersonConsumeReportNoLimit(
			Map<String, Object> map) {
		return personConsumeReportDao.countPersonConsumeReportNoLimit(map);
	}

}
