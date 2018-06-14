package com.kuangchi.sdd.elevatorConsole.devicePosition.dao;

import java.util.List;

import com.kuangchi.sdd.elevatorConsole.devicePosition.model.TKDevicePosi;


/**
 * 设备地图坐标Dao
 * @author minting.he
 *
 */
public interface ITKDevicePosiDao {

	/**
	 * 修改设备的地图坐标
	 * @author minting.he
	 * @param devicePosition
	 * @return
	 */
	public boolean updateTKDeviPosition(TKDevicePosi devicePosition);
	
	/**
	 * 删除设备的地图坐标
	 * @author minting.he
	 * @param device_num
	 * @return
	 */
	public boolean deleteTKDeviPosition(String device_num);
	
	/**
	 * 根据地图图片id查询关联的设备
	 * @author minting.he
	 * @param pic_id
	 * @return
	 */
	public List<TKDevicePosi> getTKDeviByPicId(String pic_id);
	
	/**
	 * 设备组下的梯控设备
	 * @author minting.he
	 * @param device_group_num
	 * @return
	 */
	public List<String> getTKDeviByGroupNum(String device_group_num);
	
}
