package com.kuangchi.sdd.interfaceConsole.deviceInterface.service;

import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;




/**
 * @创建人　: 高育漫
 * @创建时间: 2016-5-19 下午2:23:53
 * @功能描述: 对外接口-业务类
 */
public interface IDeviceInterfaceService {
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-4 下午1:24:53
	 * @功能描述: 根据mac查询设备信息
	 * @参数描述:
	 */
	public DeviceInfo getDeviceByMac(String device_mac);
	
	
}
