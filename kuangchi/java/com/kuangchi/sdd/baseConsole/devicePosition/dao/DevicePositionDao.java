package com.kuangchi.sdd.baseConsole.devicePosition.dao;

import java.util.List;

import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.devicePosition.model.DeviceDistributionPic;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-6-13 下午4:18:53
 * @功能描述: 设备坐标信息 - Dao类
 */
public interface DevicePositionDao {
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-13 下午4:06:13
	 * @功能描述: 设置设备坐标
	 * @参数描述:
	 */
	public void setDevicePosition(DeviceInfo device);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-16 下午3:00:40
	 * @功能描述: 删除设备坐标
	 * @参数描述:
	 */
	public void removeDevicePosition(String device_num);

	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-13 下午4:46:17
	 * @功能描述: 查询设备坐标信息
	 * @参数描述:
	 */
	public List<DeviceDistributionPic> getDevicePositionInfo();
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-14 上午10:35:35
	 * @功能描述: 根据图片ID查询关联设备
	 * @参数描述:
	 */
	public List<DeviceInfo> getDeviceByPic(String pic_id);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-15 下午2:03:20
	 * @功能描述: 根据设备组查询设备
	 * @参数描述:
	 */
	public List<String> getDeviceByDeviceGroup(String device_group);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-14 上午11:25:36
	 * @功能描述: 更新设备在线状态
	 * @参数描述:
	 */
	public void updateDeviceState(String device_num, int online_state);
	
	/**
	 * 根据设备号查询设备信息
	 * @param device_num
	 * @return
	 * @author minting.he
	 */
	public DeviceInfo getDeviceByDeviceNum(String device_num);
}
