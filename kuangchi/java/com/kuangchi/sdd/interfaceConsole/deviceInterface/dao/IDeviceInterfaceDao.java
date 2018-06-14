package com.kuangchi.sdd.interfaceConsole.deviceInterface.dao;

import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;



/**
 * @创建人　: 高育漫
 * @创建时间: 2016-5-19 下午2:21:44
 * @功能描述: 对外接口-dao类
 */
public interface IDeviceInterfaceDao {
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-4 下午1:24:53
	 * @功能描述: 根据mac查询设备信息
	 * @参数描述:
	 */
	public DeviceInfo getDeviceByMac(String device_mac);
	
	
}
