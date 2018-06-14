package com.kuangchi.sdd.commConsole.forbidDevReport.service;

public interface ForbidDevReportService {
	/**禁止设备上报接口*/
	public String setForbidDevReport(String mac,String device_type,String isStatu,String datas);
	
	/**获取禁止设备上报数据*/
	public String getForbidDevReport(String mac,String device_type);
}
