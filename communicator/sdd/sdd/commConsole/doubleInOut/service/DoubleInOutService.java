package com.kuangchi.sdd.commConsole.doubleInOut.service;

import java.util.List;

import com.kuangchi.sdd.commConsole.actualTime.model.ActualTimeBean;


public interface DoubleInOutService {
	/**
	 * 
	 * Description:设置双向进出控制
	 * date:2016年5月26日
	 * @return
	 */
	public String setDoubleInOut(String mac,String deviceType,String inOut);
	
	
	/**
	 * 
	 * Description:读取双向进出控制
	 * date:2016年5月26日
	 * @return
	 */
	public String getDoubleInOut(String mac,String deviceType);
}
