package com.kuangchi.sdd.consumeConsole.devicePosition.dao;

import java.util.List;

import com.kuangchi.sdd.baseConsole.devicePosition.model.DeviceDistributionPic;
import com.kuangchi.sdd.consumeConsole.devicePosition.model.DevicePosi;

/**
 * 设备地图坐标Dao
 * @author minting.he
 *
 */
public interface IDevicePosiDao {

	/**
	 * 修改设备的地图坐标
	 * @author minting.he
	 * @param devicePosition
	 * @return
	 */
	public boolean updateDevicePosition(DevicePosi devicePosition);
	
	/**
	 * 删除设备的地图坐标
	 * @author minting.he
	 * @param device_num
	 * @return
	 */
	public boolean deleteDevicePosition(String device_num);
	
	/**
	 * 获取所有的地图图片信息
	 * @author minting.he
	 * @return
	 */
	public List<DeviceDistributionPic> getAllPicInfo();
	
	/**
	 * 根据地图图片id查询关联的设备
	 * @author minting.he
	 * @param pic_id
	 * @return
	 */
	public List<DevicePosi> getDeviceByPicId(String pic_id);
	
	/**
	 * 查询设备组下的消费设备
	 * @author minting.he
	 * @param device_group_num
	 * @return
	 */
	public List<String> getXFDeviByGroupNum(String device_group_num);
	/**
	 * 设备组下的梯控设备
	 * @author minting.he
	 * @param device_group_num
	 * @return
	 */
	public List<String> getTKDeviByGroupNum(String device_group_num);
	
	/**
	 * 根据设备组查对应的地图
	 * @param group_num
	 * @return
	 */
	public List<DeviceDistributionPic> getPicByGroupNum(String flag, String group_num);
	
}
