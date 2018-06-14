package com.kuangchi.sdd.commConsole.device.service;

public interface IDeviceService {

	/**
	 * 清空硬件设备数据
	 * @author minting.he
	 * @param sign 设备类型
	 * @param mac MAC地址
	 * @param dataType 数据类型
	 * @return
	 */
	public boolean clearData(String sign, String mac, String dataType);
	
	/**
	 * 远程开门
	 * @author minting.he
	 * @param sign 设备类型
	 * @param mac MAC地址
	 * @param door_num 门号
	 * @return
	 */
	public boolean remoteOpenDoor(String sign, String mac, String door_num);
}
