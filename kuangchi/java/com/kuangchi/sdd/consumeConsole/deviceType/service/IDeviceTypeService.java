package com.kuangchi.sdd.consumeConsole.deviceType.service;

import java.util.List;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.consumeConsole.deviceType.model.DeviceType;

public interface IDeviceTypeService {

	/**
	 * 按条件模糊查询设备类型（分页）
	 * @param deviceType 查询参数
	 * @return
	 * @author minting.he
	 */
	public Grid<DeviceType> getDeviceTypePage(DeviceType deviceType);
	
	/**
	 * 根据id查询设备类型
	 * @param id id号
	 * @return
	 * @author minting.he
	 */
	public DeviceType selDeviceTypeById(String id);
	
	/**
	 * 设备类型编号是否存在
	 * @param device_type_num 设备类型编号
	 * @return
	 * @author minting.he
	 */
	public Integer validNum(DeviceType deviceType);
	
	/**
	 * 新增设备类型
	 * @param deviceType 
	 * @param create_user 登录用户
	 * @return
	 * @author minting.he
	 */
	public boolean insertDeviceType(DeviceType deviceType, String create_user);
	
	/**
	 * 修改设备类型
	 * @param deviceType
	 * @param create_user 登录用户
	 * @return
	 * @author minting.he
	 */
	public boolean updateDeviceType(DeviceType deviceType, String create_user);
	
	/**
	 * 删除设备类型
	 * @param ids
	 * @param create_user 登录用户
	 * @return
	 * @author minting.he
	 */
	public boolean deleteDeviceType(String ids, String create_user);
	
	/**
	 * 获取全部设备类型
	 * @return
	 */
	public List<DeviceType> getAllDeviceType();
}
