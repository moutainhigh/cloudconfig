package com.kuangchi.sdd.commConsole.gateLimit.service;

import java.util.Date;
import java.util.Map;

public interface IGetGateLimitService {
	public Map<String, Object> getGateLimit(Integer mac, Integer cardId,
			int sign) throws Exception;// 获取门禁权限

	public int setGateLimit(int mac, int gateId, int deviceType,
			String cardIdString, Date startTime, Date endTime, Integer groupNum)
			throws Exception;// 设置门禁权限

	public int delGateLimit(int mac, int gateId, int deviceType,
			String cardIdString) throws Exception;// 删除门禁权限
	
	/**
	 * 获取门禁信息
	 * by gengji.yang
	 */
	public Map readGateLimit(Integer mac,Integer cardId,int sign) throws Exception;
}
