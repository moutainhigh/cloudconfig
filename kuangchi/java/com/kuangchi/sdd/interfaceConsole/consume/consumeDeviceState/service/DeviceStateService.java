package com.kuangchi.sdd.interfaceConsole.consume.consumeDeviceState.service;

public interface DeviceStateService {
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-8-31 上午
	 * @功能描述: 修改消费设备在线状态-Service
	 */
	public boolean modifyOnlineState(String deviceNum, int onlineState);

	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-8-31 上午
	 * @功能描述: 修改消费设备忙碌状态-Service
	 */
	public boolean modifyBusyState(String deviceNum, int busyState);

	/**
	 * @创建人　: xuewen.deng
	 * @创建时间: 2016-9-5 下午
	 * @功能描述: 将消费设备改为离线线和空闲状态-Action
	 */
	public boolean initDevState();

	/**
	 * @创建人　: xuewen.deng
	 * @创建时间: 2016-12-29 下午
	 * @功能描述: 修改消费设备commIp
	 */
	public boolean modifyDevCommIp(String deviceNum, String commIpID);

}
