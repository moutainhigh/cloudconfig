package com.kuangchi.sdd.baseConsole.device.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.baseConsole.device.model.DescriptionPicModel;
import com.kuangchi.sdd.baseConsole.device.model.DeviceAttriModel;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.device.model.DeviceStateModel;
import com.kuangchi.sdd.baseConsole.device.model.EquipmentBean;
import com.kuangchi.sdd.baseConsole.deviceGroup.model.DeviceGroupModel;

public interface DeviceDao {
	/*
	 * 搜索设备状态 deviceName 设备名称 deviceNum 设备编号 skip 从第几行开始 rows 向后获取多少行
	 */
	public List<DeviceStateModel> searchDeviceState(String deviceName,
			String deviceMac, Integer skip, Integer rows);

	/*
	 * 搜索设备状态总记录数 deviceName 设备名称 deviceNum 设备编号
	 */
	public Integer searchDeviceStateCount(String deviceName, String deviceNum);

	/*
	 * 设备号精确查询名称
	 */
	public List<DeviceStateModel> exactDeviceName(String deviceNum,
			Integer skip, Integer rows);

	/*
	 * 设备号查询总行数
	 */
	public Integer exactDeviceNameCount(String deviceNum);

	/*
	 * 根据设备组搜索设备
	 */
	public List<DeviceStateModel> searchDeviceByGroup(String groupNum,
			Integer skip, Integer rows);

	/*
	 * 根据设备组搜索设备总记录行
	 */
	public Integer searchDeviceByGroupCount(String groupNum);

	/*
	 * 设置设备属性 deviceNum 设备编号 headerCardFlag 首卡开门标志 oneOutControlFlag
	 * 0、1号门双向进出控制标志 twoOutControlFlag 2、3号门双向进出控制标志 oneLockControlFlag
	 * 0、1号门互锁控制标志 twoLockControlFlag 2、3号门互锁控制标志 threeLockControlFlag
	 * 0、1、2号门互锁控制标志 fourLockControlFlag 0、1、2、3号门互锁控制标志 delayOpenDoorTime
	 * 延迟开门时间 fireFlag 消防联动 *
	 */
	public void setDeviceAttribute(String deviceNum, String headerCardFlag,
			String oneOutControlFlag, String twoOutControlFlag,
			String oneLockControlFlag, String twoLockControlFlag,
			String threeLockControlFlag, String fourLockControlFlag,
			String delayOpenDoorTime, String fireFlag);

	/*
	 * 
	 * 
	 * 按设备编号获取设备属性
	 * 
	 * deviceNum 设备编号
	 */
	public DeviceAttriModel getDeviceAttributeByDeviceNum(String deviceNum);

	/*
	 * 
	 * 按设备编号获取设备状态deviceNum 设备编号 *
	 */

	public DeviceStateModel getDeviceStateByDeviceNum(String deviceNum,
			String doorNum);

	/*
	 * 异步刷新读取设备状态 deviceNum 设备编号
	 */
	public List<DeviceStateModel> getDeviceStateByNum(String deviceNum);

	/*
	 * 查询设备属性列表 deviceName 设备名称 deviceNum 设备编号 skip 从第几行开始 rows 向后获取多少行 *
	 */

	public List<DeviceAttriModel> searchDeviceAttribute(String deviceName,
			String deviceMac, Integer skip, Integer rows);

	/*
	 * 查询设备属性列表数量 deviceName 设备名称 deviceNum 设备编号 *
	 */

	public Integer searchDeviceAttributeCount(String deviceName,
			String deviceMac);

	/*
	 * 更新设备状态 deviceNum; // 设备编号 lockState; // 锁状态 doorState; // 门状态 keyState;
	 * // 按键状态 skidState; // 防撬状态 fireState; //消防状态 *
	 */
	public void updateDeviceState(String deviceNum, String doorNum,
			String lockState, String doorState, String keyState,
			String skidState, String fireState);

	/*
	 * 添加设备状态 deviceNum; // 设备编号 lockState; // 锁状态 doorState; // 门状态 keyState;
	 * // 按键状态 skidState; // 防撬状态 fireState; //消防状态 *
	 */
	public void addDeviceState(String deviceNum, String doorNum,
			String lockState, String doorState, String keyState,
			String skidState, String fireState);

	/*
	 * 
	 * 按Mac地址获取设备编号
	 * 
	 * *
	 */
	public String getDeviceNumByMac(String mac);

	/*
	 * 
	 * 查询设备信息
	 */

	public List<DeviceInfo> getDeviceInfo(String device_name,
			String device_mac, Integer page, Integer size);

	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-4 上午11:35:53
	 * @功能描述: 根据设备编号查询设备信息
	 * @参数描述:
	 */
	public DeviceInfo getDeviceInfoByNum(String deviceNum);

	/**
	 * @创建人　: xuewen.deng
	 * @创建时间: 2017-4-26
	 * @功能描述: 根据设备mac查询设备信息
	 * @参数描述:
	 */
	public DeviceInfo getDeviceInfoByMac(String mac);

	/*
	 * 
	 * 查询设备信息条数
	 */
	public Integer getDeviceInfoCount(String device_name, String device_mac);

	/*
	 * 
	 * 修改设备信息
	 */
	public JsonResult modifyDeviceInfo(DeviceInfo deviceinfo);

	/*
	 * 
	 * 删除设备信息
	 */
	public JsonResult deleteDeviceInfo(String device_num);

	/*
	 * 
	 * 初始化设备信息
	 */
	public JsonResult initializeDeviceInfo(String device_num);

	/*
	 * 
	 * 新增设备信息
	 */
	public Integer addDeviceInfo(EquipmentBean equipmentbean);

	public String getDeviceType(String device_num);// 查询设备类型

	public Boolean insertDeviceTimeGroup(Map<String, Object> map);// 修改成功之后增加到表kc_device_time_group

	public Integer deleteDeviceTimeGroup(String device_num);// 删除门信息之后到表kc_device_time_group删除

	public Map getMacByDeviceNum(String deviceNum);

	public Integer deleteDoorPeopleauthorityService(String device_num);

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-6-8 下午15:32:05
	 * @功能描述: 查询设备分布背景图(分页)
	 */
	public List<DescriptionPicModel> getDescriptionPictures(String flag,
			String description, String device_group_num, String page,
			String size);

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-7-15 下午15:32:05
	 * @功能描述: 查询设备组
	 */
	public List<DeviceGroupModel> getDiviceGroupToPic(String flag);

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-6-8 下午15:32:05
	 * @功能描述: 查询设备分布背景图-条目数(分页)
	 */
	public Integer getDescriptionPicturesCount(String flag, String description,
			String device_group_num);

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-6-8 下午15:32:05
	 * @功能描述: 查询设备分布背景图(不分页)
	 */
	public List<DescriptionPicModel> getDescriptionPicturesNoPage(int id,
			String description, String device_group_num);

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-6-12 上午10:32:05
	 * @功能描述: 删除设备分布背景图(伪)
	 */
	public boolean deleteDescriptionPicturesByIds(String ids);

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-6-12 上午10:32:05
	 * @功能描述: 新增设备分布背景图
	 */
	public boolean addDescriptionPicture(String flag, String description,
			String pic_path, String device_group_num);

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-6-12 上午10:32:05
	 * @功能描述: 修改设备分布背景图
	 */
	public boolean updateDescriptionPicture(String description,
			String pic_path, String device_group_num, int id);

	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-30 上午11:24:41
	 * @功能描述: 更新告警事件处理状态
	 * @参数描述:
	 */
	public void updateDealState(String deviceNum, String eventDm);

	/**
	 * 根据设备号查询mac地址
	 * 
	 * @author minting.he
	 * @param device_num
	 *            设备编号
	 * @return
	 */
	public String getMacByDeviNum(String device_num);

	/**
	 * 按筛选条件导出设备信息
	 * 
	 * @param device_name
	 * @param device_mac
	 * @return
	 */
	public List<DeviceInfo> exportDevice(String device_name, String device_mac);

	/**
	 * 更新设备属性表中的 设备记录相关字段 by gengji.yang
	 */
	public void updateDeviceRecordByMac(Map map);

	public String getDoorByDeviNum(String device_mac);// 根据mac查询设备信息表中有没有该设备

	public Boolean addDoorinfo(Map<String, Object> mapp);// 搜索设备的时候直接添加门信息

	public String getDeviceByDeviNum(String device_num);// 根据设备号查询门信息表中有没有该设备

	public String getDeleteDoorByDeviNum(String device_num);// 根据设备号查询门信息表中有没有被删除的设备

	public Integer updateDeleteDoorByNum(String device_num);// 通过设备编号把被删除的门更新回来

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-8 下午5:39:09
	 * @功能描述: 获取全部时段组
	 * @参数描述:
	 */
	public List<String> getAllTimeGroups();

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-8 下午5:39:09
	 * @功能描述: 获取全部门号
	 * @参数描述:
	 */
	public List<String> getAllDoorNum();

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-8 下午5:39:09
	 * @功能描述: 获取全部设备编号
	 * @参数描述:
	 */
	public List<String> getAllDeviceNum();

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-8 下午5:39:09
	 * @功能描述: 根据设备mac地址查询对应的设备编号
	 * @参数描述:
	 */
	public String getDeviceMacByDeviceNum(String deviceMac);

	/**
	 * 设置区域联动
	 * 
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-10-26 上午11:06:56
	 * @功能描述:
	 * @参数描述:
	 */
	public boolean setOpenFireConsole(Map map);

	/**
	 * 根据mac查设备信息
	 * 
	 * @author minting.he
	 * @param device_mac
	 * @return
	 */
	public DeviceInfo getInfoByMac(String device_mac);

	/**
	 * 获取当前设备状态
	 * 
	 * @author minting.he
	 * @return
	 */
	public List<Map<String, Object>> getNowDeviceState();

	/**
	 * 定时更新设备状态
	 * 
	 * @author minting.he
	 * @param map
	 * @return
	 */
	public boolean updateEveryDeviState(Map map);

	/**
	 * 设备组是否关联到地图
	 * 
	 * @author minting.he
	 * @param map
	 * @return
	 */
	public Integer ifRelatedPic(Map map);

	/*
	 * 根据设备mac地址获取设备ip
	 */
	public DeviceInfo getDeviceIpByDeviceMac(String mac);

	public boolean updateDeviceVersion(Map map);

	/**
	 * 获取所有设备的状态
	 * 
	 * @author minting.he
	 * @return
	 */
	public List<Map<String, Object>> getAllDeviceState(List<String> list);

	/**
	 * 重置设备的地图信息
	 * 
	 * @author minting.he
	 * @param device_mac
	 * @return
	 */
	public boolean resetPosi(String device_mac);

	public List<Map<String, Object>> getAllDeviceAuthorityInfo(String device_num);// 查询权限

	public Boolean deleteAuthorityInfoByNum(String device_num);// 删除权限

	public Boolean insertHistoryAuthorityInfo(Map map);// 添加权限到历史表

	/**
	 * 通过Mac地址获取设备类型
	 * 
	 * @author minting.he
	 * @param mac
	 * @return
	 */
	public String getTypeByMac(String mac);

	/**
	 * 获取全部在线设备
	 * 
	 * @author jihui.deng
	 * @return
	 */
	public List<DeviceInfo> getOnlineDeviceInfo();

	/**
	 * 根据员工工号获取部门名称
	 * 
	 * @author xuewen.deng
	 * */
	public List<String> selectBmmc(String staff_no);

	/**
	 * 判断某个卡号是否有权限开门
	 * 
	 * @author xuewen.deng2017-05-18
	 * @param card_num
	 * @param door
	 * @param device
	 * @return
	 */
	public Integer getAuthorityCount(String card_num, String door,
			String device, String date);
}
