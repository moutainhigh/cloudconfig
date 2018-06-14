package com.kuangchi.sdd.consumeConsole.personRechargeReport.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.consumeConsole.personRechargeReport.dao.PersonRechargeReportDao;
import com.kuangchi.sdd.consumeConsole.personRechargeReport.model.PersonRechargeReportModel;
import com.kuangchi.sdd.consumeConsole.personRechargeReport.service.PersonRechargeReportService;

/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-8-26 
 * @功能描述: 人员充值报表-业务实现层
 */
@Transactional
@Service("personRechargeReportServiceImpl")
public class PersonRechargeReportServiceImpl implements PersonRechargeReportService{

	
	@Resource(name = "personRechargeReportDaoImpl")
	private PersonRechargeReportDao personRechargeReportDao;
	
	@Override
	public List<PersonRechargeReportModel> getPersonRechargeReport(
			Map<String, Object> map) {
		return personRechargeReportDao.getPersonRechargeReport(map);
	}

	@Override
	public Integer getPersonRechargeReportCount(Map<String, Object> map) {
		return personRechargeReportDao.getPersonRechargeReportCount(map);
	}

	@Override
	public List<PersonRechargeReportModel> getPersonRechargeReportNoLimit(
			Map<String, Object> map) {
		return personRechargeReportDao.getPersonRechargeReportNoLimit(map);
	}

	@Override
	public List<PersonRechargeReportModel> countPersonRechargeReport(
			Map<String, Object> map) {
		return personRechargeReportDao.countPersonRechargeReport(map);
	}

	@Override
	public Integer countPersonRechargeReportCount(Map<String, Object> map) {
		return personRechargeReportDao.countPersonRechargeReportCount(map);
	}

	@Override
	public List<PersonRechargeReportModel> countPersonRechargeReportNoLimit(
			Map<String, Object> map) {
		return personRechargeReportDao.countPersonRechargeReportNoLimit(map);
	}

}
