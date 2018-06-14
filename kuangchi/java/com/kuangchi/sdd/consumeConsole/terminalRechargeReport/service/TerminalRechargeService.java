package com.kuangchi.sdd.consumeConsole.terminalRechargeReport.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.consumeConsole.terminalRechargeReport.model.TerminalRechargeModel;

/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-8-26 
 * @功能描述: 终端充值报表-service层
 */
public interface TerminalRechargeService {

	/*获取所有终端充值报表信息（分页）*/
	public List<TerminalRechargeModel> getTerminalRechargeReport(Map<String, Object> map);
		
	/*获取所有终端充值报表信息的总数量*/
	public Integer getTerminalRechargeReportCount(Map<String, Object> map);

	/*获取所有终端充值报表信息(导出用)*/
	public List<TerminalRechargeModel> getTerminalRechargeReportNoLimit(Map<String, Object> map);
	
	/*按照筛选条件统计终端充值报表信息（分页）*/
	public List<TerminalRechargeModel> countTerminalRechargeReport(Map<String, Object> map);
	
	/*按照筛选条件统计终端充值报表信息的总数量*/
	public Integer countTerminalRechargeReportCount(Map<String, Object> map);
	
	/*按照筛选条件统计终端充值报表信息(导出用)*/
	public List<TerminalRechargeModel> countTerminalRechargeReportNoLimit(Map<String, Object> map);
}
