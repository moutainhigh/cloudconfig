package com.kuangchi.sdd.consumeConsole.deptBonusReport.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.consumeConsole.deptBonusReport.dao.DeptBonusReportDao;
import com.kuangchi.sdd.consumeConsole.deptBonusReport.model.DeptBonusReportModel;
import com.kuangchi.sdd.consumeConsole.deptBonusReport.service.DeptBonusReportService;

/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-8-29 
 * @功能描述: 部门补助报表-业务实现层
 */
@Transactional
@Service("deptBonusReportServiceImpl")
public class DeptBonusReportServiceImpl implements DeptBonusReportService{

	@Resource(name = "deptBonusReportDaoImpl")
	private DeptBonusReportDao deptBonusReportDao;
	
	@Override
	public List<DeptBonusReportModel> getDeptBonusReport(Map<String, Object> map) {
		return deptBonusReportDao.getDeptBonusReport(map);
	}

	@Override
	public Integer getDeptBonusReportCount(Map<String, Object> map) {
		return deptBonusReportDao.getDeptBonusReportCount(map);
	}

	@Override
	public List<DeptBonusReportModel> getDeptBonusReportNoLimit(
			Map<String, Object> map) {
		return deptBonusReportDao.getDeptBonusReportNoLimit(map);
	}

	@Override
	public List<DeptBonusReportModel> countDeptBonusReport(
			Map<String, Object> map) {
		return deptBonusReportDao.countDeptBonusReport(map);
	}

	@Override
	public Integer countDeptBonusReportCount(Map<String, Object> map) {
		return deptBonusReportDao.countDeptBonusReportCount(map);
	}

	@Override
	public List<DeptBonusReportModel> countDeptBonusReportNoLimit(
			Map<String, Object> map) {
		return deptBonusReportDao.countDeptBonusReportNoLimit(map);
	}

}
