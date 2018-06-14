package com.kuangchi.sdd.consumeConsole.deptConsumeReport.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.consumeConsole.deptConsumeReport.dao.DeptConsumeReportDao;
import com.kuangchi.sdd.consumeConsole.deptConsumeReport.model.DeptConsumeReportModel;
import com.kuangchi.sdd.consumeConsole.deptConsumeReport.service.DeptConsumeReportService;
import com.kuangchi.sdd.consumeConsole.goodConsumeReport.model.GoodConsumeReportModel;
import com.kuangchi.sdd.consumeConsole.goodType.model.GoodType;
import com.kuangchi.sdd.consumeConsole.vendor.model.Vendor;

/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-8-19 
 * @功能描述: 部门消费报表-业务实现层
 */
@Transactional
@Service("deptConsumeReportServiceImpl")
public class DeptConsumeReportServiceImpl implements DeptConsumeReportService{
    
	@Resource(name = "deptConsumeReportDaoImpl")
	private DeptConsumeReportDao deptConsumeReportDao;
	
	@Override
	public List<DeptConsumeReportModel> getDeptConsumeReportByParam(
			Map<String, Object> map) {
		return deptConsumeReportDao.getDeptConsumeReportByParam(map);
	}

	@Override
	public Integer getDeptConsumeReportCount(Map<String, Object> map) {
		return deptConsumeReportDao.getDeptConsumeReportCount(map);
	}

	@Override
	public List<DeptConsumeReportModel> getDeptConsumeReportNoLimit(
			Map<String, Object> map) {
		return deptConsumeReportDao.getDeptConsumeReportNoLimit(map);
	}

	@Override
	public List<DeptConsumeReportModel> initDeptConsumeReport(
			Map<String, Object> map) {
		return deptConsumeReportDao.initDeptConsumeReport(map);
	}

	@Override
	public List<DeptConsumeReportModel> initDeptConsumeReportNoLimit(
			Map<String, Object> map) {
		return deptConsumeReportDao.initDeptConsumeReportNoLimit(map);
	}
	
	@Override
	public Integer initDeptConsumeReportCount(Map<String, Object> map) {
		return deptConsumeReportDao.initDeptConsumeReportCount(map);
	}

	@Override
	public List<Vendor> getVendor() {
		return deptConsumeReportDao.getVendor();
	}

	


}
