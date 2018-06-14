package com.kuangchi.sdd.consumeConsole.terminalConsumeReport.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.consumeConsole.device.model.Device;
import com.kuangchi.sdd.consumeConsole.terminalConsumeReport.model.TerminalConsumeModel;


/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-8-25 
 * @功能描述: 终端消费报表-service层
 */
public interface TerminalConsumeReportService {

	/*获取所有终端消费报表信息（分页）*/
	public List<TerminalConsumeModel> getTerminalConsumeReport(Map<String, Object> map);
		
	/*获取所有终端消费报表信息的总数量*/
	public Integer getTerminalConsumeReportCount(Map<String, Object> map);

	/*获取所有终端消费报表信息(导出用)*/
	public List<TerminalConsumeModel> getTerminalConsumeReportNoLimit(Map<String, Object> map);
	
	/*按照筛选条件统计终端消费报表信息（分页）*/
	public List<TerminalConsumeModel> countTerminalConsumeReport(Map<String, Object> map);
	
	/*按照筛选条件统计终端消费报表信息的总数量*/
	public Integer countTerminalConsumeReportCount(Map<String, Object> map);
	
	/*按照筛选条件统计终端消费报表信息(导出用)*/
	public List<TerminalConsumeModel> countTerminalConsumeReportNoLimit(Map<String, Object> map);
	
	/*获取所有设备编号*/
	public List<Device>  getDeviceNum();
}
