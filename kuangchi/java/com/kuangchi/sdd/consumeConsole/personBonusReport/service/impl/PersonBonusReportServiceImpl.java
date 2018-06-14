package com.kuangchi.sdd.consumeConsole.personBonusReport.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.consumeConsole.personBonusReport.dao.PersonBonusReportDao;
import com.kuangchi.sdd.consumeConsole.personBonusReport.model.PersonBonusrReportModel;
import com.kuangchi.sdd.consumeConsole.personBonusReport.service.PersonBonusReportService;
import com.kuangchi.sdd.consumeConsole.personRechargeReport.dao.PersonRechargeReportDao;
import com.kuangchi.sdd.consumeConsole.personRechargeReport.service.PersonRechargeReportService;


/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-8-29 
 * @功能描述: 人员补助报表-业务实现层
 */
@Transactional
@Service("personBonusReportServiceImpl")
public class PersonBonusReportServiceImpl implements PersonBonusReportService{

	@Resource(name = "personBonusReportDaoImpl")
	private PersonBonusReportDao personBonusReportDao;

	
	@Override
	public List<PersonBonusrReportModel> getPersonBonusReport(
			Map<String, Object> map) {
		return personBonusReportDao.getPersonBonusReport(map);
	}

	@Override
	public Integer getPersonBonusReportCount(Map<String, Object> map) {
		return personBonusReportDao.getPersonBonusReportCount(map);
	}

	@Override
	public List<PersonBonusrReportModel> getPersonBonusReportNoLimit(
			Map<String, Object> map) {
		return personBonusReportDao.getPersonBonusReportNoLimit(map);
	}

	@Override
	public List<PersonBonusrReportModel> countPersonBonusReport(
			Map<String, Object> map) {
		return personBonusReportDao.countPersonBonusReport(map);
	}

	@Override
	public Integer countPersonBonusReportCount(Map<String, Object> map) {
		return personBonusReportDao.countPersonBonusReportCount(map);
	}

	@Override
	public List<PersonBonusrReportModel> countPersonBonusReportNoLimit(
			Map<String, Object> map) {
		return personBonusReportDao.countPersonBonusReportNoLimit(map);
	}
	
}
