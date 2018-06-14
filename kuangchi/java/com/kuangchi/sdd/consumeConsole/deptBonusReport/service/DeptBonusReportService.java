package com.kuangchi.sdd.consumeConsole.deptBonusReport.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.consumeConsole.deptBonusReport.model.DeptBonusReportModel;

/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-8-29 
 * @功能描述: 部门补助报表-service
 */
public interface DeptBonusReportService {

	/*获取所有部门补助报表信息（分页）*/
	public List<DeptBonusReportModel> getDeptBonusReport(Map<String, Object> map);
		
	/*获取所有部门补助报表信息的总数量*/
	public Integer getDeptBonusReportCount(Map<String, Object> map);

	/*获取所有部门补助报表信息(导出用)*/
	public List<DeptBonusReportModel> getDeptBonusReportNoLimit(Map<String, Object> map);
	
	/*按照筛选条件统计部门补助报表信息（分页）*/
	public List<DeptBonusReportModel> countDeptBonusReport(Map<String, Object> map);
	
	/*按照筛选条件统计部门补助报表信息的总数量*/
	public Integer countDeptBonusReportCount(Map<String, Object> map);
	
	/*按照筛选条件统计部门补助报表信息(导出用)*/
	public List<DeptBonusReportModel> countDeptBonusReportNoLimit(Map<String, Object> map);
}
