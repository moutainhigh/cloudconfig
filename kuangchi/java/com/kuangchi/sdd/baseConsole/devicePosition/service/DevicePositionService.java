package com.kuangchi.sdd.baseConsole.devicePosition.service;

import java.util.List;

import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.devicePosition.model.DeviceDistributionPic;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-6-13 下午4:02:41
 * @功能描述: 设备坐标信息-业务类
 */
public interface DevicePositionService {
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-13 下午4:06:13
	 * @功能描述: 设置设备坐标
	 * @参数描述:
	 */
	public void setDevicePosition(DeviceInfo device, String login_user);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-16 下午2:59:08
	 * @功能描述: 删除设备坐标
	 * @参数描述:
	 */
	public void removeDevicePosition(String device_num, String login_user);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-13 下午4:46:17
	 * @功能描述: 查询设备坐标信息
	 * @参数描述:
	 */
	public List<DeviceDistributionPic> getDevicePositionInfo(String flag, String group_num);
	
	public void updateDeviceState(String device_num, int online_state);//更新设备在线状态
	
	public List<DeviceInfo> getDeviceByPic(String pic_id);
	
	
}
