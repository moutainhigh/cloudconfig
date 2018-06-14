package com.kuangchi.sdd.consumeConsole.deviceType.dao;

import java.util.List;

import com.kuangchi.sdd.consumeConsole.deviceType.model.DeviceType;

public interface IDeviceTypeDao {
	
	/**
	 * 按条件模糊查询设备类型（分页）
	 * @param deviceType 查询参数
	 * @return
	 * @author minting.he
	 */
	public List<DeviceType> getDeviceTypePage(DeviceType deviceType);
	
	/**
	 * 按条件查询设备类型总数
	 * @param deviceType 查询参数
	 * @return
	 * @author minting.he
	 */
	public Integer getDeviceTypeCounts(DeviceType deviceType);
	
	/**
	 * 根据id查询设备类型
	 * @param id id号
	 * @return
	 * @author minting.he
	 */
	public DeviceType selDeviceTypeById(String id);
	
	/**
	 * 设备类型编号是否存在
	 * @param deviceType 设备类型
	 * @return
	 * @author minting.he
	 */
	public Integer validNum(DeviceType deviceType);
	
	/**
	 * 新增设备类型
	 * @param deviceType
	 * @return
	 * @author minting.he
	 */
	public boolean insertDeviceType(DeviceType deviceType);
	
	/**
	 * 修改设备类型
	 * @param deviceType
	 * @return
	 * @author minting.he
	 */
	public boolean updateDeviceType(DeviceType deviceType);
	
	/**
	 * 删除设备类型
	 * @param ids
	 * @return
	 * @author minting.he
	 */
	public boolean deleteDeviceType(String ids);
	
	/**
	 * 获取全部设备类型
	 * @return
	 */
	public List<DeviceType> getAllDeviceType();
}
