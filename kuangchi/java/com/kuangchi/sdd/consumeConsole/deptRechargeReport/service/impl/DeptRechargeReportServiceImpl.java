package com.kuangchi.sdd.consumeConsole.deptRechargeReport.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.consumeConsole.deptRechargeReport.dao.DeptRechargeReportDao;
import com.kuangchi.sdd.consumeConsole.deptRechargeReport.model.DeptRechargeReportModel;
import com.kuangchi.sdd.consumeConsole.deptRechargeReport.service.DeptRechargeReportService;

/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-8-26 
 * @功能描述: 部门充值报表-业务实现层
 */
@Transactional
@Service("deptRechargeReportServiceImpl")
public class DeptRechargeReportServiceImpl implements DeptRechargeReportService{

	@Resource(name = "deptRechargeReportDaoImpl")
	private DeptRechargeReportDao deptRechargeReportDao;
	
	@Override
	public List<DeptRechargeReportModel> getDeptRechargeReportByParam(
			Map<String, Object> map) {
		return deptRechargeReportDao.getDeptRechargeReportByParam(map);
	}

	@Override
	public Integer getDeptRechargeReportCount(Map<String, Object> map) {
		return deptRechargeReportDao.getDeptRechargeReportCount(map);
	}

	@Override
	public List<DeptRechargeReportModel> getDeptRechargeReportNoLimit(
			Map<String, Object> map) {
		return deptRechargeReportDao.getDeptRechargeReportNoLimit(map);
	}

	@Override
	public List<DeptRechargeReportModel> countDeptRechargeReport(
			Map<String, Object> map) {
		return deptRechargeReportDao.countDeptRechargeReport(map);
	}

	@Override
	public Integer countDeptRechargeReportCount(Map<String, Object> map) {
		return deptRechargeReportDao.countDeptRechargeReportCount(map);
	}

	@Override
	public List<DeptRechargeReportModel> countDeptRechargeReportNoLimit(
			Map<String, Object> map) {
		return deptRechargeReportDao.countDeptRechargeReportNoLimit(map);
	}
	
	

}
