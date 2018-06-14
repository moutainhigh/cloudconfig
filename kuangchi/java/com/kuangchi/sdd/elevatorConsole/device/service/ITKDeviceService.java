package com.kuangchi.sdd.elevatorConsole.device.service;

import java.util.List;
import java.util.Map;

import com.google.gson.internal.LinkedTreeMap;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.model.easyui.Tree;
import com.kuangchi.sdd.elevatorConsole.device.model.Device;
import com.kuangchi.sdd.elevatorConsole.device.model.Floor;
import com.kuangchi.sdd.elevatorConsole.device.model.Holiday;
import com.kuangchi.sdd.elevatorConsole.device.model.OpenTimeZone;
import com.kuangchi.sdd.elevatorConsole.device.model.TkDevAuthorityCardModel;

/**
 * 设备信息维护Service
 */
public interface ITKDeviceService {

	/**
	 * 梯控设备分页
	 * @author minting.he
	 * @param device
	 * @return
	 */
	public Grid<Device> getTKDeviceListParam(Device device);

	/**
	 * 获取设备组、设备的树
	 * @author minting.he
	 * @return
	 */
	public Tree deviceAndGroupTree();

	/**
	 * 点击树查看设备
	 * @author minting.he
	 * @param device
	 * @return
	 */
	public Grid<Device> getTKDeviceByTree(Device device);

	/**
	 * 修改设备关联的设备组号
	 * @author minting.he
	 * @param device_group_num
	 * @param device_num
	 * @param login_user
	 * @return
	 */
	public boolean changeTKDeviceGroup(String device_group_num,
			String device_num, String login_user);

	/**
	 * 搜索后新增设备
	 * @author minting.he
	 * @param device
	 * @return
	 */
	public Integer seekTKDevice(List<Device> devices, String login_user);

	/**
	 * 获取设备配置的楼层
	 * @author minting.he
	 * @param device_num
	 * @return
	 */
	public List<Floor> getDeviceFloor(String device_num);

	/**
	 * 加载楼层名称
	 * @author minting.he
	 * @param device_num
	 * @return
	 */
	public List<Floor> getFloorName(String device_num);

	/**
	 * 删除梯控设备
	 * @author minting.he
	 * @param device_num
	 * @return
	 */
	public boolean deleteTKDevice(String device_num, String login_user);

	/**
	 * 根据设备号获取设备信息
	 * @author minting.he
	 * @param device_num
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
	public boolean updateTKDevice(Device device, String login_user);

	/**
	 * 更新设备状态
	 * @author minting.he
	 * @param device
	 * @return
	 */
	public boolean updateTKDevcieState(Device device);

	/**
	 * 修改设备配置楼层
	 * @author minting.he
	 * @param floor
	 * @param login_user
	 * @return
	 */
	public boolean updateDeviceFloor(List<Floor> floors,String login_user);

	/**
	 * 设置楼层开放时区
	 * 
	 * @param floors
	 * @param login_user
	 * @return
	 */
	public boolean setFloorOpenArea(List<Floor> floors, String login_user);

	/**
	 * 查看楼层信息
	 * @author minting.he
	 * @param device_num
	 * @return
	 */
	public List<Floor> getFloorInfo(String device_num);

	/**
	 * 复制配置楼层
	 * @author minting.he
	 * @param device_num
	 * @param copy_device_num
	 * @param login_user
	 * @return
	 */
	public boolean copyConfiFloor(String device_num, String copy_device_num);

	/**
	 * 复制楼层开放时区
	 * @author minting.he
	 * @param device_num
	 * @param copy_device_num
	 * @param login_user
	 * @return
	 */
	public boolean copyFloorOpenArea(String device_num, String copy_device_num);

	/**
	 * 查询设备的节假日
	 * @author minting.he
	 * @param map
	 * @return
	 */
	public Grid<Holiday> getHolidayByDevice(Map<String, Object> map);

	/**
	 * 下发节假日
	 * @author minting.he
	 * @param device_num
	 * @param holiday_nums
	 * @param login_user
	 * @return
	 */
	public boolean issuedHoliday(String device_num, List<Holiday> holidayList,
			String login_user);

	/**
	 * 复制节假日
	 * @author minting.he
	 * @param holiday
	 * @return
	 */
	public boolean copyHoliday(String device_num, String copy_device_num);

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
	 * 查询设备所有节假日
	 * @author minting.he
	 * @param device_num
	 * @return
	 */
	public List<Holiday> getHolisByDevice(String device_num);

	/**
	 * 读取配置楼层接口
	 * @author minting.he
	 * @param device_num
	 * @return
	 */
	public LinkedTreeMap commReadFloor(String device_num);

	/**
	 * 配置楼层连接接口
	 * @author minting.he
	 * @param device_num
	 * @param floorList
	 * @return
	 */
	public boolean commConfigFloor(String device_num, List<Integer> config);

	/**
	 * 楼层开放时区接口
	 * @author minting.he
	 * @param device_num
	 * @param floorList
	 * @return
	 */
	public boolean commFloorOpen(String device_num, Floor floor);

	/**
	 * 节假日连接接口
	 * @author minting.he
	 * @param device_num
	 * @param holidayDateList
	 * @return
	 */
	public boolean commHoliday(String device_num, List<String> holidayDateList);

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-11-1 下午1:18:04
	 * @功能描述: 清除节假日
	 * @参数描述:
	 */
	boolean clearHoliday(String device_num, List holiList, String login_user);

	/**
	 * @param login_user
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-11-2 下午1:24:37
	 * @功能描述: 清除楼层开放时区
	 * @参数描述:
	 */
	public boolean cleanFloorOpenArea(Floor floor, String login_user);

	/**
	 * 修改后重新下发
	 * 
	 * @param device_num
	 * @param holiday0
	 *            原节假日
	 * @param holidayList
	 * @param login_user
	 * @return
	 */
	public boolean issuedHoliForModify(String device_num, String holiday0,
			List<Holiday> holidayList, String login_user);

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-11-10 下午3:34:28
	 * @功能描述: 清除电梯开放时区
	 * @参数描述:
	 */
	public boolean updateDeviceOpenTime(OpenTimeZone openTimeZone,
			Device device);
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-11-10 下午3:47:56
	 * @功能描述: 清除权限
	 * @参数描述:
	 */
	boolean clearAuthorityByDevNum(String device_num,List<TkDevAuthorityCardModel> list,String login_user);
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-11-10 下午3:47:56
	 * @功能描述: 清除动作参数
	 * @参数描述:
	 */
	boolean setEleMoveParam(Device device,Map<String, Integer> map);
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-11-10 下午3:47:56
	 * @功能描述: 修改配置楼层(仅下发到设备)
	 * @参数描述:
	 */
	public boolean updateDeviceFloorComm(List<Floor> floors, String login_user);

	/**
	 * @param  
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-11-10 下午3:47:56
	 * @功能描述: 复制动作参数
	 * @参数描述:
	 */
	public boolean copyEleMoveParam(Map<String, Object> map,String copy_device_num);
	
	/**
	 * @param  
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-11-10 下午3:47:56
	 * @功能描述: 复制电梯开放时区
	 * @参数描述:
	 */
	public boolean copyEleOpenTime(String device_num, String copy_device_num);
	
	/**
	 * 获取是否启用的楼层
	 * @author minting.he
	 * @param device_num
	 * @return
	 */
	public List<Map<String, Object>> getFlagFloor(String device_num);
	
	/**
	 * 设置是否启用楼层
	 * @author minting.he
	 * @param list
	 * @param login_user
	 * @return
	 * @throws Exception 
	 */
	public boolean setIfEnabledFloor(List<Map<String, Object>> list, String login_user) throws Exception;
	
	/**
	 * 获取启用/不启用的楼层
	 * @author minting.he
	 * @param map
	 * @return
	 */
	public List<String> getIfEnabledFloor(Map<String, Object> map);
	
}
