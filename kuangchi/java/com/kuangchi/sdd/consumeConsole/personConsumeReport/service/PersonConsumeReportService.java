package com.kuangchi.sdd.consumeConsole.personConsumeReport.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.consumeConsole.personConsumeReport.model.PersonConsumeReportModel;


/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-8-24 
 * @功能描述: 人员消费报表-service
 */
public interface PersonConsumeReportService {
  
	/*获取所有人员消费报表信息（分页）*/
	public List<PersonConsumeReportModel> getPersonConsumeReport(Map<String, Object> map);
		
	/*获取所有人员消费报表信息的总数量*/
	public Integer getPersonConsumeReportCount(Map<String, Object> map);

	/*获取所有人员消费报表信息(导出用)*/
	public List<PersonConsumeReportModel> getPersonConsumeReportNoLimit(Map<String, Object> map);
	
	/*按照筛选条件统计人员消费报表信息（分页）*/
	public List<PersonConsumeReportModel> countPersonConsumeReport(Map<String, Object> map);
	
	/*按照筛选条件统计人员消费报表信息的总数量*/
	public Integer countPersonConsumeReportCount(Map<String, Object> map);
	
	/*按照筛选条件统计人员消费报表信息(导出用)*/
	public List<PersonConsumeReportModel> countPersonConsumeReportNoLimit(Map<String, Object> map);
	
}
