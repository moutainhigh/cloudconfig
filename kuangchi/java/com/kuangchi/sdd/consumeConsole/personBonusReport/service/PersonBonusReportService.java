package com.kuangchi.sdd.consumeConsole.personBonusReport.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.consumeConsole.personBonusReport.model.PersonBonusrReportModel;

/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-8-29 
 * @功能描述: 人员补助报表-service
 */
public interface PersonBonusReportService {

	/*获取所有人员补助报表信息（分页）*/
	public List<PersonBonusrReportModel> getPersonBonusReport(Map<String, Object> map);
		
	/*获取所有人员补助报表信息的总数量*/
	public Integer getPersonBonusReportCount(Map<String, Object> map);

	/*获取所有人员补助报表信息(导出用)*/
	public List<PersonBonusrReportModel> getPersonBonusReportNoLimit(Map<String, Object> map);
	
	/*按照筛选条件统计人员补助报表信息（分页）*/
	public List<PersonBonusrReportModel> countPersonBonusReport(Map<String, Object> map);
	
	/*按照筛选条件统计人员补助报表信息的总数量*/
	public Integer countPersonBonusReportCount(Map<String, Object> map);
	
	/*按照筛选条件统计人员补助报表信息(导出用)*/
	public List<PersonBonusrReportModel> countPersonBonusReportNoLimit(Map<String, Object> map);
}
