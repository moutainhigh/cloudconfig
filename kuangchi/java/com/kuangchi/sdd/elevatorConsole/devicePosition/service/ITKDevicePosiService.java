package com.kuangchi.sdd.elevatorConsole.devicePosition.service;

import java.util.List;

import com.kuangchi.sdd.baseConsole.devicePosition.model.DeviceDistributionPic;
import com.kuangchi.sdd.elevatorConsole.devicePosition.model.TKDevicePosi;

/**
 * 设备地图坐标Service
 * @author minting.he
 *
 */
public interface ITKDevicePosiService {

	/**
	 * 修改设备的地图坐标
	 * @author minting.he
	 * @param devicePosi
	 * @param login_user
	 * @return
	 */
	public boolean updateTKDeviPosition(TKDevicePosi devicePosi, String login_user);
	
	/**
	 * 删除设备的地图坐标
	 * @author minting.he
	 * @param device_num
	 * @param login_user
	 * @return
	 */
	public boolean deleteTKDeviPosition(String device_num, String login_user);
	
	/**
	 * 获取地图和设备信息
	 * @author minting.he
	 * @return
	 */
	public List<DeviceDistributionPic> getPicInfo(String flag, String group_num);
	
}
