package com.kuangchi.sdd.commConsole.trigger.service;

import com.kuangchi.sdd.comm.equipment.common.GateParamData;

/**
 * 获取门禁控制器的接口服务
 * @author 
 *
 */
public interface ITriggerService {
	/**
	 * 
	 * Description:设置上位促发开门
	 * date:2016年5月11日
	 * @return
	 */
	public String setTrigger(String mac,String clientTrigger);
}
