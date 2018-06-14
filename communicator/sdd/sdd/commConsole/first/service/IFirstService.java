package com.kuangchi.sdd.commConsole.first.service;

import com.kuangchi.sdd.comm.equipment.common.GateParamData;

/**
 * 获取门禁控制器的接口服务
 * @author 
 *
 */
public interface IFirstService {
	/**
	 * 
	 * Description:设置首卡开门
	 * date:2016年5月11日
	 * @return
	 */

	public String setFirst(String mac, String sign, String gateId, String first);
}
