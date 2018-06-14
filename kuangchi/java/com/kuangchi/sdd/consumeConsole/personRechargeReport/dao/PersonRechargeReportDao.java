package com.kuangchi.sdd.consumeConsole.personRechargeReport.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.consumeConsole.personRechargeReport.model.PersonRechargeReportModel;


/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-8-26 
 * @功能描述: 人员充值报表-dao
 */
public interface PersonRechargeReportDao {
	
	/*获取所有人员充值报表信息（分页）*/
	public List<PersonRechargeReportModel> getPersonRechargeReport(Map<String, Object> map);
		
	/*获取所有人员充值报表信息的总数量*/
	public Integer getPersonRechargeReportCount(Map<String, Object> map);

	/*获取所有人员充值报表信息(导出用)*/
	public List<PersonRechargeReportModel> getPersonRechargeReportNoLimit(Map<String, Object> map);
	
	/*按照筛选条件统计人员充值报表信息（分页）*/
	public List<PersonRechargeReportModel> countPersonRechargeReport(Map<String, Object> map);
	
	/*按照筛选条件统计人员充值报表信息的总数量*/
	public Integer countPersonRechargeReportCount(Map<String, Object> map);
	
	/*按照筛选条件统计人员充值报表信息(导出用)*/
	public List<PersonRechargeReportModel> countPersonRechargeReportNoLimit(Map<String, Object> map);

}
