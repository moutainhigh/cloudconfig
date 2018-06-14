package com.kuangchi.sdd.elevatorConsole.device.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.consumeConsole.device.model.DeviceGroup;
import com.kuangchi.sdd.elevatorConsole.device.model.Device;
import com.kuangchi.sdd.elevatorConsole.device.model.Floor;
import com.kuangchi.sdd.elevatorConsole.device.model.Holiday;

/**
 * 设备信息维护Dao
 */
public interface ITKDeviceDao {

	/**
	 * 梯控设备分页
	 * @author minting.he
	 * @param device
	 * @return
	 */
	public List<Device> getTKDeviceListParam(Device device);

	/**
	 * 梯控设备分页总数
	 * @author minting.he
	 * @param device
	 * @return
	 */
	public Integer getTKDeviceListParamCount(Device device);

	/**
	 * 查询所有设备组
	 * @author minting.he
	 * @return
	 */
	public List<DeviceGroup> getTKDeviceGroup();

	/**
	 * 查询所有设备
	 * @author minting.he
	 * @return
	 */
	public List<Device> getAllTKDevice();

	/**
	 * 点击树查看设备
	 * @author minting.he
	 * @param device
	 * @return
	 */
	public List<Device> getTKDeviceByTree(Device device);

	/**
	 * 点击树查看设备总数
	 * @author minting.he
	 * @param device
	 * @return
	 */
	public Integer getTKDeviceByTreeCount(Device device);

	/**
	 * 修改设备关联的设备组编号
	 * @author minting.he
	 * @param device_group_num
	 * @param device_num
	 * @return
	 */
	public boolean changeTKDeviceGroup(String device_group_num,
			String device_num);

	/**
	 * 搜索后新增设备
	 * @author minting.he
	 * @param device
	 * @return
	 */
	public boolean insertTKDevice(Device device);

	/**
	 * 设备MAC是否存在
	 * @author minting.he
	 * @param mac
	 * @return
	 */
	public Integer ifExistTKDeviMac(Device device);

	/**
	 * 设备配置默认楼层
	 * @author minting.he
	 * @param floor
	 * @return
	 */
	public boolean insertDefaultFloor(Floor floor);

	/**
	 * 删除梯控设备
	 * @author minting.he
	 * @param device_num
	 * @return
	 */
	public boolean deleteTKDevice(String device_num);

	/**
	 * 删除设备楼层
	 * @author minting.he
	 * @param device_num
	 * @return
	 */
	public boolean delDeviceFloor(String device_num);

	/**
	 * 删除设备节假日
	 * @author minting.he
	 * @param device_num
	 * @return
	 */
	public boolean delDeviceHoliday(String device_num);

	/**
	 * 根据设备号获取设备信息
	 * @author minting.he
	 * @param device
	 * @return
	 */
	public Device getInfoByTKDeviceNum(String device_num);

	/**
	 * 设备名称是否存在
	 * @author minting.he
	 * @param device_name
	 * @return
	 */
	public Integer ifExistTKDeviName(Map<String, Object> map);

	/**
	 * 修改梯控设备
	 * @author minting.he
	 * @param device
	 * @return
	 */
	public boolean updateTKDevice(Device device);

	/**
	 * 更新设备时间
	 * @author minting.he
	 * @param mac
	 * @return
	 */
	public boolean updateDeviceTime(String mac);

	/**
	 * 更新设备状态
	 * @author minting.he
	 * @param device
	 * @return
	 */
	public boolean updateTKDeviceState(Device device);

	/**
	 * 获取设备配置楼层
	 * @author minting.he
	 * @param deviec_num
	 * @return
	 */
	public List<Floor> getDeviceFloor(String deviec_num);

	/**
	 * 修改设备配置楼层
	 * @author minting.he
	 * @param floor
	 * @return
	 */
	public boolean updateDeviceFloor(Floor floor);

	/**
	 * 设置楼层开放时区
	 * @author minting.he
	 * @param floor
	 * @return
	 */
	public boolean setFloorOpenArea(Floor floor);

	/**
	 * 查看楼层信息
	 * @author minting.he
	 * @param floor
	 * @return
	 */
	public List<Floor> getFloorInfo(String device_num);

	/**
	 * 查询设备的节假日分页
	 * @author minting.he
	 * @param device_num
	 * @return
	 */
	public List<Holiday> getHolidayByDevice(Map<String, Object> map);

	/**
	 * 查询设备的节假日总数
	 * @author minting.he
	 * @param map
	 * @return
	 */
	public Integer getHolidayByDeviceCount(Map<String, Object> map);

	/**
	 * 查询设备节假日
	 * @author minting.he
	 * @param device_num
	 * @return
	 */
	public List<Holiday> getHolisByDevi(String device_num);

	/**
	 * 复制节假日
	 * @author minting.he
	 * @return
	 */
	public boolean insertHoliday(Holiday holiday);

	/**
	 * 更新所有设备的状态
	 * @author minting.he
	 * @param online_state
	 * @return
	 */
	public boolean updateAllState(String online_state);

	/**
	 * 获取所有的设备信息
	 * @author minting.he
	 * @return
	 */
	public List<Device> getAllTKDeviceInfo();

	/**
	 * 删除设备节假日
	 * @author minting.he
	 * @param device_num
	 * @return
	 */
	public boolean delDeviceHoli(String device_num);

	/**
	 * 更新节假日下发状态
	 * @author xuewen.deng
	 * @param state
	 * @return
	 */
	public boolean updateHoliSend_State(Holiday holiday, int state);

	/**
	 * 获取已经下发过的节假日
	 * @author minting.he
	 * @param device_num
	 * @return
	 */
	public List<Holiday> getSendHoliday(String device_num);
	
	/**
	 * 重置设备的地图信息
	 * @author minting.he
	 * @param mac
	 * @return
	 */
	public boolean resetPosition(String mac);
	
	/**
	 * 获取是否启用的楼层
	 * @author minting.he
	 * @param device_num
	 * @return
	 */
	public List<Map<String, Object>> getFlagFloor(String device_num);
	
	/**
	 * 设置楼层是否启用
	 * @author minting.he
	 * @param map
	 * @return
	 */
	public boolean setIfEnabledFloor(Map<String, Object> map);
	
	/**
	 * 获取启用/不启用的楼层
	 * @author minting.he
	 * @param map
	 * @return
	 */
	public List<String> getIfEnabledFloor(Map<String, Object> map);
	
	/**
	 * 获取启用的楼层名称
	 * @author minting.he
	 * @param device_num
	 * @return
	 */
	public List<Floor> getFloorName(String device_num);
	
	
}
