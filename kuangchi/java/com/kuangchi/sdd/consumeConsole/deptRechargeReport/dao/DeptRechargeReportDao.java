package com.kuangchi.sdd.consumeConsole.deptRechargeReport.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.consumeConsole.deptRechargeReport.model.DeptRechargeReportModel;



/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-8-26 
 * @功能描述: 部门充值报表-dao
 */
public interface DeptRechargeReportDao {
	
	/*获取所有部门充值报表信息（分页）*/
	public List<DeptRechargeReportModel> getDeptRechargeReportByParam(Map<String, Object> map);
		
	/*获取所有部门充值报表信息的总数量*/
	public Integer getDeptRechargeReportCount(Map<String, Object> map);

	/*获取所有部门充值报表信息(导出用)*/
	public List<DeptRechargeReportModel> getDeptRechargeReportNoLimit(Map<String, Object> map);

	/*按筛选条件获取部门充值报表信息*/
	public List<DeptRechargeReportModel> countDeptRechargeReport(Map<String, Object> map);

	/*按筛选条件获取部门充值报表信息总数量*/
	public Integer countDeptRechargeReportCount(Map<String, Object> map);

	/*按筛选条件获取部门充值报表信息(导出用)*/
	public List<DeptRechargeReportModel> countDeptRechargeReportNoLimit(Map<String, Object> map);

}
