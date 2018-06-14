package com.kuangchi.sdd.baseConsole.device.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.device.model.DescriptionPicModel;
import com.kuangchi.sdd.baseConsole.device.model.DeviceAttriModel;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.device.model.DeviceStateModel;
import com.kuangchi.sdd.baseConsole.device.model.EquipmentBean;
import com.kuangchi.sdd.baseConsole.device.model.ResultMsg;
import com.kuangchi.sdd.baseConsole.device.model.TimeResultMsg;
import com.kuangchi.sdd.baseConsole.deviceGroup.model.DeviceGroupModel;

public interface DeviceService {
	/*
	 * 搜索设备状态 deviceName 设备名称 deviceNum 设备编号 skip 从第几行开始 rows 向后获取多少行
	 */
	public Grid<DeviceStateModel> searchDeviceState(String deviceName,
			String deviceMac, Integer skip, Integer rows);

	/*
	 * 设备号精确查询名称
	 */
	public Grid<DeviceStateModel> exactDeviceName(String deviceNum,
			Integer skip, Integer rows);

	/*
	 * 根据设备组搜索设备
	 */
	public Grid<DeviceStateModel> searchDeviceByGroup(String groupNum,
			Integer skip, Integer rows);

	/*
	 * 设置设备属性 deviceNum 设备编号 headerCardFlag 首卡开门标志 oneOutControlFlag
	 * 0、1号门双向进出控制标志 twoOutControlFlag 2、3号门双向进出控制标志 oneLockControlFlag
	 * 0、1号门互锁控制标志 twoLockControlFlag 2、3号门互锁控制标志 threeLockControlFlag
	 * 0、1、2号门互锁控制标志 fourLockControlFlag 0、1、2、3号门互锁控制标志 delayOpenDoorTime
	 * 延迟开门时间 fireFlag 消防联动 deviceCNum 设备记录数 keepNum 巡更记录数 userNum 用户记录数
	 * deviceRootNum 设备权限个数 keepRootNum 巡更权限个数 *
	 */
	public void setDeviceAttribute(String deviceNum, String headerCardFlag,
			String oneOutControlFlag, String twoOutControlFlag,
			String oneLockControlFlag, String twoLockControlFlag,
			String threeLockControlFlag, String fourLockControlFlag,
			String delayOpenDoorTime, String fireFlag, String createUser);

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

	public DeviceStateModel getDeviceStateByDeviceNum(String deviceNum);

	/*
	 * 异步刷新读取设备状态 deviceNum 设备编号
	 */
	public DeviceStateModel getDeviceStateByNum(String doorNum, String deviceNum);

	/*
	 * 查询设备属性列表 deviceName 设备名称 deviceNum 设备编号 skip 从第几行开始 rows 向后获取多少行 *
	 */

	public Grid<DeviceAttriModel> searchDeviceAttribute(String deviceName,
			String deviceMac, Integer skip, Integer rows);

	/*
	 * 更新设备状态 mac; // 设备Mac地址 lockState; // 锁状态 doorState; // 门状态 keyState; //
	 * 按键状态 skidState; // 防撬状态 fireState; //消防状态 *
	 */
	public void updateDeviceState(String mac, String doorNum, String lockState,
			String doorState, String keyState, String skidState,
			String fireState, String createUser);

	/*
	 * 
	 * 查询设备信息
	 */

	public List<DeviceInfo> getDeviceInfo(String device_name,
			String device_mac, Integer page, Integer size);

	/*
	 * 
	 * 查询设备信息条数
	 */
	public Integer getDeviceInfoCount(String device_name, String device_mac);

	/*
	 * 
	 * 修改设备信息
	 */
	public JsonResult modifyDeviceInfo(DeviceInfo deviceinfo, String admin_id);

	/*
	 * 
	 * 删除设备信息
	 */
	public JsonResult deleteDeviceInfo(String device_num, String admin_id);

	/*
	 * 
	 * 初始化设备信息
	 */
	public JsonResult initializeDeviceInfo(String device_num,
			String device_mac, String device_type, String admin_id);

	/*
	 * 
	 * 新增设备信息
	 */
	public Integer addDeviceInfo(EquipmentBean equipmentbean);

	public Map getMacByDeviceNum(String deviceNum);

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-7-15 下午15:32:05
	 * @功能描述: 查询设备组
	 */
	public List<DeviceGroupModel> getDiviceGroupToPic(String flag);

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
	 * @创建时间: 2016-6-8 下午15:32:05
	 * @功能描述: 查询设备分布背景图-条目数(分页)
	 */
	public Integer getDescriptionPicturesCount(String flag, String description,
			String device_group_num);

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-6-12 上午10:32:05
	 * @功能描述: 删除设备分布背景图(伪)
	 */
	public boolean deleteDescriptionPicturesByIds(String ids);

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
	 * @创建时间: 2016-7-4 上午11:35:53
	 * @功能描述: 根据设备编号查询设备信息
	 * @参数描述:
	 */
	public DeviceInfo getDeviceInfoByNum(String deviceNum);

	/**
	 * 初始化设备时间组
	 * 
	 * @param doorType
	 * @param admin_id
	 * @param mac
	 */
	public void setDeviceTimeGroup(String doorType, String admin_id, String mac);

	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-30 上午11:36:57
	 * @功能描述: 更新告警事件处理状态
	 * @参数描述:
	 */
	public void updateDealState(String deviceNum, String eventDm);

	/**
	 * 根据设备号查询mac地址
	 * 
	 * @author minting.he
	 * @param device_num
	 *            设备号
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
	 * 清空硬件设备数据
	 * 
	 * @author minting.he
	 * @param sign
	 *            设备类型
	 * @param mac
	 *            MAC地址
	 * @param dataType
	 *            数据类型 1 门禁记录 4 刷卡记录 8 门禁权限
	 * @return
	 */
	public boolean clearDeviceData(String sign, String mac, String dataType,
			String login_user);

	/**
	 * 远程开门
	 * 
	 * @author minting.he
	 * @param doors
	 *            门号
	 * @param devices
	 *            设备号
	 * @return
	 */
	public boolean remoteOpenDoor(String doors, String devices,
			String login_user);

	/**
	 * 远程开门
	 * 
	 * @author xuewen.deng
	 * @param doors
	 *            门号
	 * @param devices
	 *            设备号
	 * @return
	 */
	public boolean qrCodeRemoteOpenDoor(String doors, String devices,
			String login_user, String card_num, String staff_name,
			String staff_no);

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-7-14 下午2:37:32
	 * @功能描述:获取设备校时时间
	 * @参数描述:
	 */
	public TimeResultMsg getDevTime(String mac, String device_type);

	/**
	 * 
	 * Description:设置设备时间 date:2016年7月5日
	 * 
	 * @param mac
	 *            设备mac地址
	 * @param sign
	 *            设备类型
	 * @return
	 */
	public ResultMsg setDeviceTime2(String mac, String device_type);

	/**
	 * 
	 * Description:设置设备时间 date:2016年7月6日
	 * 
	 * @param mac
	 *            设备mac地址
	 * @param sign
	 *            设备类型
	 * @return
	 */
	public ResultMsg restartDevice(String mac, String device_type);

	public void updateDeviceRecord(String mac, List<Map> list);

	public String getDoorByDeviNum(String device_num);

	public Boolean addDoorinfo(Map<String, Object> mapp);// 搜索设备的时候直接添加门信息

	public String getDeviceByDeviNum(String device_num);// 根据设备号查询门信息表中有没有该设备

	public String getDeleteDoorByDeviNum(String device_num);// 根据设备号查询门信息表中有没有被删除的设备

	public Integer updateDeleteDoorByNum(String device_num);// 通过设备编号把被删除的门更新回来

	/**
	 * 
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-10-8 下午1:32:17
	 * @功能描述: 根据设备mac地址查询对应的设备编号
	 * @参数描述:
	 */
	public String getDeviceNumByDeviceMac(String deviceMac);

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-8 下午5:38:03
	 * @功能描述: 获取全部时段组
	 * @参数描述:
	 */
	public List<String> getAllTimeGroups();

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-9 下午3:26:06
	 * @功能描述:获取全部门号
	 * @参数描述:
	 */
	public List<String> getAllDoorNum();

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-9 下午3:26:52
	 * @功能描述:获取全部设备编号
	 * @参数描述:
	 */
	public List<String> getAllDeviceNum();

	/**
	 * 软件版本更新
	 * 
	 * @author xuewen.deng
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public boolean uploadDeviceVersion(String mac, String device_type, File file)
			throws FileNotFoundException, IOException;

	public boolean setOpenFireConsole(Map strMap, String login_user);

	/**
	 * 设备族是否关联到地图
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

	/* 更新设备版本 */
	public boolean updateDeviceVersion(Map map, String login_user);

	/**
	 * 获取所有设备的状态
	 * 
	 * @author minting.he
	 * @return
	 */
	public List<Map<String, Object>> getAllDeviceState(String devices);

	/**
	 * 重置设备的地图信息
	 * 
	 * @author minting.he
	 * @param device_mac
	 * @return
	 */
	public boolean resetPosi(String device_mac);

	/**
	 * 获取所有在线设备信息
	 * 
	 * @author jihui.deng
	 * @return
	 */
	public List<DeviceInfo> getOnlineDeviceInfo();

	/**
	 * 判断某个卡号是否有权限开门
	 * 
	 * @author xuewen.deng2017-05-18
	 * @param card_num
	 * @param door
	 * @param deviceMac
	 * @return
	 */
	public boolean isAuthorize(String card_num, String door, String deviceMac);

}
