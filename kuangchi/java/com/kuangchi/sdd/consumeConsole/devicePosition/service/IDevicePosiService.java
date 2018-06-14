package com.kuangchi.sdd.consumeConsole.devicePosition.service;

import java.util.List;

import com.kuangchi.sdd.baseConsole.devicePosition.model.DeviceDistributionPic;
import com.kuangchi.sdd.consumeConsole.devicePosition.model.DevicePosi;

/**
 * 设备地图坐标Service
 * @author minting.he
 *
 */
public interface IDevicePosiService {

	/**
	 * 修改设备的地图坐标
	 * @author minting.he
	 * @param devicePosi
	 * @param login_user
	 * @return
	 */
	public boolean updateDevicePosition(DevicePosi devicePosi, String login_user);
	
	/**
	 * 删除设备的地图坐标
	 * @author minting.he
	 * @param device_num
	 * @param login_user
	 * @return
	 */
	public boolean deleteDevicePosition(String device_num, String login_user);
	
	/**
	 * 获取地图和设备信息
	 * @author minting.he
	 * @return
	 */
	public List<DeviceDistributionPic> getPicInfo(String flag, String group_num);
	
}
