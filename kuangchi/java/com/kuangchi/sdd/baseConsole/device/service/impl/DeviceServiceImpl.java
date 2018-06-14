package com.kuangchi.sdd.baseConsole.device.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base32;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.device.dao.DeviceDao;
import com.kuangchi.sdd.baseConsole.device.model.DescriptionPicModel;
import com.kuangchi.sdd.baseConsole.device.model.DeviceAttriModel;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.device.model.DeviceStateModel;
import com.kuangchi.sdd.baseConsole.device.model.EquipmentBean;
import com.kuangchi.sdd.baseConsole.device.model.ResultMsg;
import com.kuangchi.sdd.baseConsole.device.model.TimeResultMsg;
import com.kuangchi.sdd.baseConsole.device.service.DeviceService;
import com.kuangchi.sdd.baseConsole.deviceGroup.model.DeviceGroupModel;
import com.kuangchi.sdd.baseConsole.event.dao.EventDao;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.baseConsole.mjComm.service.MjCommService;
import com.kuangchi.sdd.baseConsole.quartz.model.Result;
import com.kuangchi.sdd.doorAccessConsole.authority.dao.PeopleAuthorityInfoDao;
import com.kuangchi.sdd.util.cacheUtils.CacheUtils;
import com.kuangchi.sdd.util.commonUtil.DateUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;
import com.kuangchi.sdd.util.datastructure.BoundedQueueUsedForDisplay;

@Service("deviceService")
public class DeviceServiceImpl implements DeviceService {

	public static final Logger LOG = Logger.getLogger(DeviceServiceImpl.class);

	@Resource(name = "deviceDao")
	DeviceDao deviceDao;
	@Resource(name = "peopleAuthorityDao")
	private PeopleAuthorityInfoDao peopleAuthorityDao;
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;

	@Resource(name = "mjCommServiceImpl")
	private MjCommService mjCommService;

	/* 查询设备状态 weixuan.lu 2016/3/28 */
	@Override
	public Grid<DeviceStateModel> searchDeviceState(String deviceName,
			String deviceMac, Integer skip, Integer rows) {
		Grid<DeviceStateModel> grid = new Grid<DeviceStateModel>();
		List<DeviceStateModel> resultList = deviceDao.searchDeviceState(
				deviceName, deviceMac, skip, rows);
		if (resultList != null) {
			for (DeviceStateModel deviceInfo : resultList) {
				if (deviceInfo.getDeviceMac().length() == 1) {
					deviceInfo
							.setDeviceMac("00000" + deviceInfo.getDeviceMac());
				} else if (deviceInfo.getDeviceMac().length() == 2) {
					deviceInfo.setDeviceMac("0000" + deviceInfo.getDeviceMac());
				} else if (deviceInfo.getDeviceMac().length() == 3) {
					deviceInfo.setDeviceMac("000" + deviceInfo.getDeviceMac());
				} else if (deviceInfo.getDeviceMac().length() == 4) {
					deviceInfo.setDeviceMac("00" + deviceInfo.getDeviceMac());
				} else if (deviceInfo.getDeviceMac().length() == 5) {
					deviceInfo.setDeviceMac("0" + deviceInfo.getDeviceMac());
				} else {
					deviceInfo.setDeviceMac(deviceInfo.getDeviceMac());
				}
				deviceInfo
						.setDeviceMac(deviceInfo.getDeviceMac().toUpperCase());
			}
			grid.setRows(resultList);
			grid.setTotal(deviceDao.searchDeviceStateCount(deviceName,
					deviceMac));
		} else {
			grid.setTotal(0);
		}
		return grid;
	}

	/*
	 * 设备号精确查询名称
	 */
	public Grid<DeviceStateModel> exactDeviceName(String deviceNum,
			Integer skip, Integer rows) {
		Grid<DeviceStateModel> grid = new Grid<DeviceStateModel>();
		List<DeviceStateModel> resultList = deviceDao.exactDeviceName(
				deviceNum, skip, rows);
		grid.setRows(resultList);
		if (null != resultList) {
			grid.setTotal(deviceDao.exactDeviceNameCount(deviceNum));
		} else {
			grid.setTotal(0);
		}
		return grid;
	}

	/*
	 * 根据设备组搜索设备
	 */
	@Override
	public Grid<DeviceStateModel> searchDeviceByGroup(String groupNum,
			Integer skip, Integer rows) {
		Grid<DeviceStateModel> grid = new Grid<DeviceStateModel>();
		List<DeviceStateModel> resultList = deviceDao.searchDeviceByGroup(
				groupNum, skip, rows);

		grid.setRows(resultList);
		if (null != resultList) {
			grid.setTotal(deviceDao.searchDeviceByGroupCount(groupNum));
		} else {
			grid.setTotal(0);
		}
		return grid;
	}

	@Override
	public void setDeviceAttribute(String deviceNum, String headerCardFlag,
			String oneOutControlFlag, String twoOutControlFlag,
			String oneLockControlFlag, String twoLockControlFlag,
			String threeLockControlFlag, String fourLockControlFlag,
			String delayOpenDoorTime, String fireFlag, String createUser) {

		deviceDao.setDeviceAttribute(deviceNum, headerCardFlag,
				oneOutControlFlag, twoOutControlFlag, oneLockControlFlag,
				twoLockControlFlag, threeLockControlFlag, fourLockControlFlag,
				delayOpenDoorTime, fireFlag);
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "设备属性设置");
		log.put("V_OP_FUNCTION", "修改业务");
		log.put("V_OP_ID", createUser);
		log.put("V_OP_MSG", "复制设备联动");
		log.put("V_OP_TYPE", "业务");
		logDao.addLog(log);

	}

	@Override
	public DeviceAttriModel getDeviceAttributeByDeviceNum(String deviceNum) {

		return deviceDao.getDeviceAttributeByDeviceNum(deviceNum);
	}

	/* 根据设备编号获取设备状态 */
	@Override
	public DeviceStateModel getDeviceStateByDeviceNum(String deviceNum) {
		return deviceDao.getDeviceStateByDeviceNum(deviceNum, null);
	}

	/**
	 * 异步刷新读取设备状态
	 * 
	 * @return
	 */
	public DeviceStateModel getDeviceStateByNum(String deviceNum, String doorNum) {
		DeviceStateModel state = deviceDao.getDeviceStateByDeviceNum(deviceNum,
				doorNum);
		return state;
	}

	@Override
	public Grid<DeviceAttriModel> searchDeviceAttribute(String deviceName,
			String deviceMac, Integer skip, Integer rows) {
		Grid<DeviceAttriModel> grid = new Grid<DeviceAttriModel>();
		List<DeviceAttriModel> resultList = deviceDao.searchDeviceAttribute(
				deviceName, deviceMac, skip, rows);
		grid.setRows(resultList);
		if (null != resultList) {
			grid.setTotal(deviceDao.searchDeviceAttributeCount(deviceName,
					deviceMac));
		} else {
			grid.setTotal(0);
		}
		return grid;
	}

	@Override
	public void updateDeviceState(String mac, String doorNum, String lockState,
			String doorState, String keyState, String skidState,
			String fireState, String createUser) {

		String deviceNum = deviceDao.getDeviceNumByMac(mac);
		DeviceStateModel deviceStateModel = deviceDao
				.getDeviceStateByDeviceNum(deviceNum, doorNum);

		if (deviceStateModel != null) {
			deviceDao.updateDeviceState(deviceNum, doorNum, lockState,
					doorState, keyState, skidState, fireState);

			Map<String, String> log = new HashMap<String, String>();
			log.put("V_OP_NAME", "更新设备状态");
			log.put("V_OP_FUNCTION", "修改业务");
			log.put("V_OP_ID", createUser);
			log.put("V_OP_MSG", "更新设备状态");
			log.put("V_OP_TYPE", "业务");
			logDao.addLog(log);
		}
	}

	@Override
	public List<DeviceInfo> getDeviceInfo(String device_name,
			String device_mac, Integer page, Integer size) {
		return deviceDao.getDeviceInfo(device_name, device_mac, page, size);
	}

	@Override
	public Integer getDeviceInfoCount(String device_name, String device_mac) {
		return deviceDao.getDeviceInfoCount(device_name, device_mac);
	}

	@Transactional
	@Override
	public JsonResult modifyDeviceInfo(DeviceInfo deviceinfo, String admin_id) {
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "修改设备信息");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", admin_id);
		log.put("V_OP_TYPE", "业务");
		JsonResult result = new JsonResult();
		try {
			/*
			 * Map<String, String> mappro = PropertiesToMap
			 * .propertyToMap("comm_interface.properties");
			 */
			String mac = deviceinfo.getDevice_mac();
			String deviceType = deviceinfo.getDevice_type();
			Map<String, String> param = new HashMap<String, String>();
			param.put("mechineIP", deviceinfo.getLocal_ip_address());
			param.put("remoteIP", deviceinfo.getRemote_id_address());
			param.put("mechineStatusPort", deviceinfo.getLocal_state_port());
			param.put("remoteStatusPort", deviceinfo.getRemote_state_port());
			param.put("mechineOrderPort", deviceinfo.getLocal_order_port());
			param.put("remoteOrderPort", deviceinfo.getRemote_order_port());
			param.put("mechineEventPort", deviceinfo.getLocal_event_port());
			param.put("remoteEventPort", deviceinfo.getRemote_event_port());
			param.put("mask", deviceinfo.getSubnet_mask());
			param.put("gateway", deviceinfo.getGateway_param());
			if (deviceinfo.getCardsign().equals("8")) {
				param.put("cardSign", "4");
			} else if (deviceinfo.getCardsign().equals("4")) {
				param.put("cardSign", "3");
			} else {
				param.put("cardSign", deviceinfo.getCardsign());
			}

			String deviceNum = mjCommService.getTkDevNumByMac(mac);

			String data = GsonUtil.toJson(param);
			String str = HttpRequest.sendPost(
					mjCommService.getMjCommUrl(deviceNum)
							+ "init/initEquipment.do?", "data=" + data
							+ "&mac=" + mac + "&deviceType=" + deviceType);
			LOG.info("返回的结果" + str);
			Map msg = GsonUtil.toBean(str, HashMap.class);
			if (msg != null) {
				if ("0".equals(msg.get("result_code"))) {
					result = deviceDao.modifyDeviceInfo(deviceinfo);
					log.put("V_OP_MSG", "修改设备信息成功");
					logDao.addLog(log);
				} else {
					log.put("V_OP_MSG", "修改设备信息失败");
					logDao.addLog(log);
					throw new RuntimeException();// 注意写回滚注解
				}
			} else {
				log.put("V_OP_MSG", "修改设备信息失败，连接异常");
				logDao.addLog(log);
				throw new RuntimeException();// 注意写回滚注解
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_MSG", "修改设备信息失败，异常");
			logDao.addLog(log);
			result.setSuccess(false);
			return result;
		}
		if (result.isSuccess() == false) {
			log.put("V_OP_MSG", "修改设备信息失败");
		}
		return result;
	}

	@Override
	public JsonResult deleteDeviceInfo(String device_num, String admin_id) {
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "删除设备信息");
		log.put("V_OP_FUNCTION", "删除");
		log.put("V_OP_ID", admin_id);
		log.put("V_OP_TYPE", "业务");
		JsonResult result = new JsonResult();

		try {
			result = deviceDao.deleteDeviceInfo(device_num);
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_MSG", "删除设备信息失败，异常");
			logDao.addLog(log);
			result.setSuccess(false);
			return result;
		}
		if (result.isSuccess() == false) {
			log.put("V_OP_MSG", "删除设备信息失败");
		}
		log.put("V_OP_MSG", "删除设备信息成功");
		logDao.addLog(log);
		deviceDao.deleteDeviceTimeGroup(device_num);
		deviceDao.deleteDoorPeopleauthorityService(device_num);
		return result;
	}

	@Transactional
	@Override
	public JsonResult initializeDeviceInfo(String device_num,
			String device_mac, String device_type, String admin_id) {
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "初始化设备信息");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", admin_id);
		log.put("V_OP_TYPE", "业务");
		JsonResult result = new JsonResult();
		try {
			/*
			 * Map<String, String> mappro = PropertiesToMap
			 * .propertyToMap("comm_interface.properties");
			 */
			String str = HttpRequest.sendPost(
					mjCommService.getMjCommUrl(device_num)
							+ "init/resetEquipment.do?", "&mac=" + device_mac
							+ "&deviceType=" + device_type);
			Map msg = GsonUtil.toBean(str, HashMap.class);

			if (msg != null) {
				if ("0".equals(msg.get("result_code"))) {
					result = deviceDao.initializeDeviceInfo(device_num);
					log.put("V_OP_MSG", "初始化设备信息成功");
					logDao.addLog(log);
					// 根据设备编号查询出权限表中的数据
					List<Map<String, Object>> list = deviceDao
							.getAllDeviceAuthorityInfo(device_num);
					// 初始化成功之后删除权限表中的数据
					deviceDao.deleteAuthorityInfoByNum(device_num);
					// 添加权限到历史表
					if (list.size() != 0) {
						Map<String, Object> map = new HashMap<String, Object>();
						for (Map<String, Object> map2 : list) {
							map.put("card_num", map2.get("card_num"));
							map.put("door_num", map2.get("door_num"));
							map.put("device_num", map2.get("device_num"));
							map.put("device_mac", device_mac);
							map.put("device_type", device_type);
							map.put("valid_start_time",
									map2.get("valid_start_time"));
							map.put("valid_end_time",
									map2.get("valid_end_time"));
							map.put("time_group_num",
									map2.get("time_group_num"));
							map.put("result_flag", 0);
							map.put("tryTimes", 1);
							deviceDao.insertHistoryAuthorityInfo(map);
						}
					}
				} else {
					log.put("V_OP_MSG", "初始化设备信息失败");
					logDao.addLog(log);
					throw new RuntimeException();// 注意写回滚注解
				}
			} else {
				log.put("V_OP_MSG", "初始化设备信息失败，连接异常");
				logDao.addLog(log);
				throw new RuntimeException();// 注意写回滚注解
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_MSG", "初始化设备信息失败，异常");
			logDao.addLog(log);
			result.setSuccess(false);
			return result;
		}
		return result;
	}

	@Override
	public Integer addDeviceInfo(EquipmentBean equipmentbean) {
		// Map<String,Object> map=new HashMap<String, Object>();

		return deviceDao.addDeviceInfo(equipmentbean);

	}

	@Override
	public Map getMacByDeviceNum(String deviceNum) {
		return deviceDao.getMacByDeviceNum(deviceNum);
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-6-8 下午15:32:05
	 * @功能描述: 查询设备分布背景图(分页)
	 */
	public List<DescriptionPicModel> getDescriptionPictures(String flag,
			String description, String device_group_num, String page,
			String size) {
		return deviceDao.getDescriptionPictures(flag, description,
				device_group_num, page, size);
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-6-8 下午15:32:05
	 * @功能描述: 查询设备分布背景图-条目数(分页)
	 */
	public Integer getDescriptionPicturesCount(String flag, String description,
			String device_group_num) {
		return deviceDao.getDescriptionPicturesCount(flag, description,
				device_group_num);
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-6-12 上午10:32:05
	 * @功能描述: 删除设备分布背景图(伪)
	 */
	public boolean deleteDescriptionPicturesByIds(String ids) {
		return deviceDao.deleteDescriptionPicturesByIds(ids);
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-6-8 下午15:32:05
	 * @功能描述: 查询设备分布背景图(不分页)
	 */
	public List<DescriptionPicModel> getDescriptionPicturesNoPage(int id,
			String description, String device_group_num) {
		return deviceDao.getDescriptionPicturesNoPage(id, description,
				device_group_num);
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-6-12 上午10:32:05
	 * @功能描述: 新增设备分布背景图
	 */
	public boolean addDescriptionPicture(String flag, String description,
			String pic_path, String device_group_num) {
		return deviceDao.addDescriptionPicture(flag, description, pic_path,
				device_group_num);
	}

	@Override
	public boolean updateDescriptionPicture(String description,
			String pic_path, String device_group_num, int id) {
		return deviceDao.updateDescriptionPicture(description, pic_path,
				device_group_num, id);
	}

	@Override
	public DeviceInfo getDeviceInfoByNum(String deviceNum) {

		return deviceDao.getDeviceInfoByNum(deviceNum);
	}

	// 初始化设备时间组
	public void setDeviceTimeGroup(String doorType, String admin_id, String mac) {
		String deviceNum = deviceDao.getDeviceNumByMac(mac);
		if ("4".equals(doorType)) {
			deviceDao.deleteDeviceTimeGroup(deviceNum);
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 1; i < 5; i++) {
				map.put("door_num", String.valueOf(i));
				for (int j = 1; j < 9; j++) {
					map.put("device_num", deviceNum);
					map.put("time_order", j);
					map.put("validity_flag", "0");
					map.put("create_user", admin_id);
					deviceDao.insertDeviceTimeGroup(map);
				}
			}
		} else if ("1".equals(doorType)) {
			deviceDao.deleteDeviceTimeGroup(deviceNum);
			for (int i = 1; i < 9; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("device_num", deviceNum);
				map.put("validity_flag", "0");
				map.put("time_order", i);
				map.put("create_user", admin_id);
				map.put("door_num", "1");
				deviceDao.insertDeviceTimeGroup(map);
			}
		} else if ("2".equals(doorType)) {
			deviceDao.deleteDeviceTimeGroup(deviceNum);
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 1; i < 3; i++) {
				map.put("door_num", String.valueOf(i));
				for (int j = 1; j < 9; j++) {
					map.put("device_num", deviceNum);
					map.put("time_order", j);
					map.put("validity_flag", "0");
					map.put("create_user", admin_id);
					deviceDao.insertDeviceTimeGroup(map);
				}
			}
		}
	}

	@Override
	public void updateDealState(String deviceNum, String eventDm) {
		deviceDao.updateDealState(deviceNum, eventDm);
	}

	@Override
	public String getMacByDeviNum(String device_num) {
		return deviceDao.getMacByDeviNum(device_num);
	}

	@Override
	public List<DeviceInfo> exportDevice(String device_name, String device_mac) {
		return deviceDao.exportDevice(device_name, device_mac);
	}

	@Override
	public boolean clearDeviceData(String sign, String mac, String dataType,
			String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		try {
			String[] datas = dataType.split(",");

			String deviceNum = mjCommService.getTkDevNumByMac(mac);

			for (int i = 0; i < datas.length; i++) {
				dataType = datas[i];
				// 连接接口
				String url = mjCommService.getMjCommUrl(deviceNum);
				String message = HttpRequest.sendPost(url
						+ "device/clearDeviceData.do?", "mac=" + mac + "&sign="
						+ sign + "&dataType=" + dataType);
				Result result = GsonUtil.toBean(message, Result.class);
				if (result != null) {
					if ("0".equals(result.getResult_code())) { // 连接成功
						if ("8".equals(dataType)) { // 清空门禁权限
							// 根据设备编号查询出权限表中的数据
							List<Map<String, Object>> list = deviceDao
									.getAllDeviceAuthorityInfo(deviceNum);
							// 添加权限到历史表
							if (list.size() != 0) {
								Map<String, Object> map = new HashMap<String, Object>();
								for (Map<String, Object> map2 : list) {
									map.put("card_num", map2.get("card_num"));
									map.put("door_num", map2.get("door_num"));
									map.put("device_num",
											map2.get("device_num"));
									map.put("device_mac", mac);
									map.put("device_type",
											deviceDao.getTypeByMac(mac));
									map.put("valid_start_time",
											map2.get("valid_start_time"));
									map.put("valid_end_time",
											map2.get("valid_end_time"));
									map.put("time_group_num",
											map2.get("time_group_num"));
									map.put("result_flag", 0);
									map.put("tryTimes", 1);
									deviceDao.insertHistoryAuthorityInfo(map);
								}
							}
							// 删除权限表中的数据
							deviceDao.deleteAuthorityInfoByNum(deviceNum);
						}
					}
				} else {
					log.put("V_OP_TYPE", "异常");
					return false;
				}
			}
			log.put("V_OP_TYPE", "业务");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally {
			log.put("V_OP_NAME", "清空门禁设备数据");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "清空门禁设备数据");
			logDao.addLog(log);
		}
	}

	@Value("${redisConnectIp}")
	private String redisConnectIp;
	@Value("${redisConnectPort}")
	private String redisConnectPort;
	@Resource(name = "eventDao")
	private EventDao eventDao;

	@Override
	public boolean remoteOpenDoor(String doors, String devices,
			String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		boolean r = false;
		try {
			String[] door = doors.split(",");
			String[] device = devices.split(",");
			for (int i = 0; i < door.length; i++) {
				if (door[i] != null && !"".equals(door[i])) {
					String device_num = device[i];
					DeviceInfo deviceInfo = deviceDao
							.getDeviceInfoByMac(device_num);
					if ("0".equals(deviceInfo.getOnline_state())) { // 设备在线时才开门
						// 连接接口
						String url = mjCommService.getMjCommUrl(device_num);
						String message = HttpRequest.sendPost(url
								+ "device/remoteOpenDoor.do?", "mac="
								+ deviceInfo.getDevice_mac() + "&sign="
								+ deviceInfo.getDevice_type() + "&door_num="
								+ door[i]);
						Result result = GsonUtil.toBean(message, Result.class);
						if (result == null
								|| "1".equals(result.getResult_code())) {
							log.put("V_OP_TYPE", "异常");
							r = false;
						} else {
							log.put("V_OP_TYPE", "业务");
							r = true;

							// 在redis中显示
							Date date = new Date();
							SimpleDateFormat dateFormat1 = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							String checktime = dateFormat1.format(date);

							CacheUtils<Map<String, Object>> cacheEvent = new CacheUtils<Map<String, Object>>(
									redisConnectIp,
									Integer.valueOf(redisConnectPort));
							CacheUtils<BoundedQueueUsedForDisplay> cacheQueue = new CacheUtils<BoundedQueueUsedForDisplay>(
									redisConnectIp,
									Integer.valueOf(redisConnectPort));
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("card_num", "");
							map.put("checktime", checktime);
							map.put("device_mac", deviceInfo.getDevice_mac());
							map.put("record_id", Math.random());
							map.put("flag_status", "0");
							map.put("event_dm", "100");
							map.put("device_num", device_num);
							map.put("device_ip",
									deviceInfo.getLocal_ip_address());
							map.put("event_type", "100");
							map.put("event_desc", null);
							map.put("device_name", deviceInfo.getDevice_name());
							map.put("event_name",
									eventDao.getEventNameByDm("100"));
							map.put("staff_name", "");
							map.put("staff_num", "");
							map.put("staff_no", "");
							map.put("dept_no", "");
							map.put("dept_name", "");
							map.put("log_type", "");
							map.put("door_num", door[i]);
							map.put("door_name", eventDao.getDoorNameByNum(map));

							cacheEvent.pushObject("eventList", map);

							BoundedQueueUsedForDisplay q = cacheQueue
									.getObject("doorSysQueue");
							if (q == null) {
								q = new BoundedQueueUsedForDisplay(50);
							}
							q.offer(map);
							cacheQueue.saveObject("doorSysQueue", q);
							BoundedQueueUsedForDisplay l = cacheQueue
									.getObject("doorSysLatest");
							if (l == null) {
								l = new BoundedQueueUsedForDisplay(5);
							}
							l.offer(map);
							cacheQueue.saveObject("doorSysLatest", l);
						}
					}
				}
			}
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally {
			log.put("V_OP_NAME", "设备监控管理");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "远程开门");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean qrCodeRemoteOpenDoor(String doors, String devices,
			String login_user, String card_num, String staff_name,
			String staff_no) {
		Map<String, String> log = new HashMap<String, String>();
		boolean r = false;
		try {
			String[] door = doors.split(",");
			String[] device = devices.split(",");
			for (int i = 0; i < door.length; i++) {
				if (door[i] != null && !"".equals(door[i])) {
					DeviceInfo deviceInfo = deviceDao
							.getDeviceInfoByMac(device[i]);
					String device_num = deviceInfo.getDevice_num();
					if (isAuthorize(card_num, door[i], device[i])
							&& "0".equals(deviceInfo.getOnline_state())) { // 设备在线且有权限时才开门
						// 连接接口
						String url = mjCommService.getMjCommUrl(device_num);
						String message = HttpRequest.sendPost(url
								+ "device/remoteOpenDoor.do?", "mac="
								+ deviceInfo.getDevice_mac() + "&sign="
								+ deviceInfo.getDevice_type() + "&door_num="
								+ door[i]);
						Result result = GsonUtil.toBean(message, Result.class);
						if (result == null
								|| "1".equals(result.getResult_code())) {
							log.put("V_OP_TYPE", "异常");
							r = false;
						} else {
							log.put("V_OP_TYPE", "业务");
							r = true;

							// 在redis中显示
							Date date = new Date();
							SimpleDateFormat dateFormat1 = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							String checktime = dateFormat1.format(date);

							CacheUtils<Map<String, Object>> cacheEvent = new CacheUtils<Map<String, Object>>(
									redisConnectIp,
									Integer.valueOf(redisConnectPort));
							CacheUtils<BoundedQueueUsedForDisplay> cacheQueue = new CacheUtils<BoundedQueueUsedForDisplay>(
									redisConnectIp,
									Integer.valueOf(redisConnectPort));
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("card_num", card_num == null ? ""
									: card_num);
							map.put("checktime", checktime);
							map.put("device_mac", deviceInfo.getDevice_mac());
							map.put("record_id", Math.random());
							map.put("flag_status", "0");
							map.put("event_dm", "100");
							map.put("device_num", device_num);
							map.put("device_ip",
									deviceInfo.getLocal_ip_address());
							map.put("event_type", "100");
							map.put("event_desc", null);
							map.put("device_name", deviceInfo.getDevice_name());
							map.put("event_name",
									eventDao.getEventNameByDm("100"));
							map.put("staff_name", staff_name == null ? ""
									: staff_name);
							map.put("staff_num", "");
							map.put("staff_no", staff_no == null ? ""
									: staff_no);
							map.put("dept_no", "");
							List<String> bmmcList = deviceDao
									.selectBmmc(staff_no == null ? ""
											: staff_no);// 根据员工工号获取部门名称
							if (bmmcList != null && bmmcList.size() > 0) {
								map.put("dept_name",
										bmmcList.get(0) == null ? "" : bmmcList
												.get(0));
							} else {
								map.put("dept_name", "");
							}
							map.put("log_type", "");
							map.put("door_num", door[i]);
							map.put("door_name", eventDao.getDoorNameByNum(map));

							cacheEvent.pushObject("eventList", map);

							BoundedQueueUsedForDisplay q = cacheQueue
									.getObject("doorSysQueue");
							if (q == null) {
								q = new BoundedQueueUsedForDisplay(50);
							}
							q.offer(map);
							cacheQueue.saveObject("doorSysQueue", q);
							BoundedQueueUsedForDisplay l = cacheQueue
									.getObject("doorSysLatest");
							if (l == null) {
								l = new BoundedQueueUsedForDisplay(5);
							}
							l.offer(map);
							cacheQueue.saveObject("doorSysLatest", l);
						}
					}
				}
			}
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally {
			log.put("V_OP_NAME", "设备监控管理");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "远程开门");
			logDao.addLog(log);
		}
	}

	@Override
	public ResultMsg setDeviceTime2(String mac, String device_type) {
		String deviceNum = mjCommService.getTkDevNumByMac(mac);

		String url = mjCommService.getMjCommUrl(deviceNum) + "time/setTime2.do";
		ResultMsg result = null;
		ResultMsg result2 = new ResultMsg();
		try {
			String str = HttpRequest.sendPost(url, "mac=" + mac
					+ "&device_type=" + device_type);
			result = GsonUtil.toBean(str, ResultMsg.class);
			if (result == null) {
				result2.setResult_code("1");
				result2.setResult_msg("校时失败");
			} else {
				result2 = result;
			}
		} catch (Exception e) {
			result2.setResult_code("1");
			result2.setResult_msg("校时失败");
			e.printStackTrace();
		}
		return result2;
	}

	@Override
	public ResultMsg restartDevice(String mac, String device_type) {
		String deviceNum = mjCommService.getTkDevNumByMac(mac);

		String url = mjCommService.getMjCommUrl(deviceNum)
				+ "device/restartDevice.do";
		ResultMsg result = null;
		ResultMsg result2 = new ResultMsg();
		try {
			String str = HttpRequest.sendPost(url, "mac=" + mac
					+ "&device_type=" + device_type);
			result = GsonUtil.toBean(str, ResultMsg.class);
			if (result == null) {
				result2.setResult_code("1");
				result2.setResult_msg("复位失败");
			} else {
				result2 = result;
			}
		} catch (Exception e) {
			result2.setResult_code("1");
			result2.setResult_msg("复位失败");
			e.printStackTrace();
		}
		return result2;
	}

	@Override
	public void updateDeviceRecord(String mac, List<Map> list) {
		if (list != null) {
			for (Map map : list) {
				map.put("mac", mac);
				deviceDao.updateDeviceRecordByMac(map);
			}
		}
	}

	@Override
	public String getDoorByDeviNum(String device_mac) {

		return deviceDao.getDoorByDeviNum(device_mac);
	}

	@Override
	public Boolean addDoorinfo(Map<String, Object> mapp) {
		return deviceDao.addDoorinfo(mapp);
	}

	@Override
	public String getDeviceByDeviNum(String device_num) {
		return deviceDao.getDeviceByDeviNum(device_num);
	}

	@Override
	public String getDeleteDoorByDeviNum(String device_num) {
		return deviceDao.getDeleteDoorByDeviNum(device_num);
	}

	@Override
	public Integer updateDeleteDoorByNum(String device_num) {
		return deviceDao.updateDeleteDoorByNum(device_num);
	}

	@Override
	public TimeResultMsg getDevTime(String mac, String device_type) {
		String deviceNum = mjCommService.getTkDevNumByMac(mac);

		String url = mjCommService.getMjCommUrl(deviceNum) + "time/getTime.do";
		TimeResultMsg result = new TimeResultMsg();
		String str = HttpRequest.sendPost(url, "mac=" + mac + "&device_type="
				+ device_type);
		if (!"".equals(str) && null != str) {
			result = GsonUtil.toBean(str, TimeResultMsg.class);
		} else {
			result.setResult_msg("校时失败");
			result.setResult_code("1");
		}
		return result;
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-7-15 下午15:32:05
	 * @功能描述: 查询设备组
	 */
	public List<DeviceGroupModel> getDiviceGroupToPic(String flag) {
		return deviceDao.getDiviceGroupToPic(flag);
	}

	@Override
	public String getDeviceNumByDeviceMac(String deviceMac) {
		return deviceDao.getDeviceMacByDeviceNum(deviceMac);
	}

	@Override
	public List<String> getAllTimeGroups() {
		return deviceDao.getAllTimeGroups();
	}

	@Override
	public List<String> getAllDoorNum() {
		return deviceDao.getAllDoorNum();
	}

	@Override
	public List<String> getAllDeviceNum() {
		return deviceDao.getAllDeviceNum();
	}

	@Override
	public boolean uploadDeviceVersion(String mac, String device_type, File file)
			throws IOException {
		String deviceNum = mjCommService.getTkDevNumByMac(mac);

		String url1 = mjCommService.getMjCommUrl(deviceNum)
				+ "upLoadVersion/downSoftware.do";
		String str1 = HttpRequest.sendPost(url1, "mac=" + mac + "&device_type="
				+ device_type);
		if (str1 != null) {
			if ("0".equals(str1)) {

				InputStream fis = null;
				fis = new FileInputStream(file);

				long fileLength = file.length() - 16; // 最后16 个字节为MD5指纹
				long packageSize = 898;
				long residue = fileLength % packageSize;
				long loopTimes = residue == 0 ? fileLength / packageSize
						: (fileLength / packageSize + 1);
				int packageNum = 1;
				String url2 = mjCommService.getMjCommUrl(deviceNum)
						+ "upLoadVersion/packageDownSoftware.do";
				Base32 base32 = new Base32();// 用base32进行编码解码
				for (int i = 0; i < loopTimes - 1; i++) {

					byte[] bytes = new byte[Long.valueOf(packageSize)
							.intValue()];
					fis.read(bytes);
					String isoString = new String(base32.encode(bytes), "utf-8");
					String str2 = HttpRequest.sendPost(url2, "mac=" + mac
							+ "&device_type=" + device_type + "&packageNum="
							+ packageNum + "&isoString=" + isoString);
					/*
					 * if (str2 == null || !("0".equals(str2))) { return false;
					 * }
					 */
					packageNum++;
				}

				int residueLength = (residue == 0 ? Long.valueOf(packageSize)
						.intValue() : Long.valueOf(residue).intValue());

				byte[] bytes2 = new byte[residue == 0 ? Long.valueOf(
						packageSize).intValue() : Long.valueOf(residue)
						.intValue()];
				fis.read(bytes2);

				// String isoString2 = new String(bytes2, "ISO-8859-1");
				String isoString2 = new String(base32.encode(bytes2), "utf-8");
				// 尾字节
				String url3 = mjCommService.getMjCommUrl(deviceNum)
						+ "upLoadVersion/packageStern.do";
				String str3 = HttpRequest.sendPost(url3, "mac=" + mac
						+ "&device_type=" + device_type + "&residueLength="
						+ residueLength + "&packageNum=" + packageNum
						+ "&isoString=" + isoString2);

				if (null == str3 || "0".equals(str3)) {
					String url4 = mjCommService.getMjCommUrl(deviceNum)
							+ "upLoadVersion/overSoftware.do";
					String str4 = HttpRequest.sendPost(url4, "mac=" + mac
							+ "&device_type=" + device_type);
					if (null == str4 || "0".equals(str4)) {
						return true;
					} else {
						return false;
					}

				}
			}
		}
		return false;
	}

	@Override
	public boolean setOpenFireConsole(Map strMap, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		boolean result = false;
		log.put("V_OP_NAME", "设备监控管理");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", login_user);
		log.put("V_OP_MSG", "消防联动");

		try {
			result = deviceDao.setOpenFireConsole(strMap);
			if (result) {
				log.put("V_OP_TYPE", "业务");
			} else {
				log.put("V_OP_TYPE", "异常");
			}
			logDao.addLog(log);
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			logDao.addLog(log);
		}
		return result;

	}

	@Override
	public Integer ifRelatedPic(Map map) {
		return deviceDao.ifRelatedPic(map);
	}

	@Override
	public DeviceInfo getDeviceIpByDeviceMac(String mac) {
		return deviceDao.getDeviceIpByDeviceMac(mac);
	}

	@Override
	public boolean updateDeviceVersion(Map map, String login_user) {
		Map<String, String> log = new HashMap<String, String>();
		boolean result = false;
		log.put("V_OP_NAME", "设备信息管理");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", login_user);
		log.put("V_OP_MSG", "版本更新");

		result = deviceDao.updateDeviceVersion(map);
		if (result) {
			log.put("V_OP_TYPE", "业务");
		} else {
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		return result;
	}

	@Override
	public List<Map<String, Object>> getAllDeviceState(String device_num) {
		try {
			String[] device = device_num.split(",");
			List<String> list = new ArrayList<String>();
			for (String num : device) {
				list.add(num);
			}
			return deviceDao.getAllDeviceState(list);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean resetPosi(String device_mac) {
		try {
			return deviceDao.resetPosi(device_mac);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<DeviceInfo> getOnlineDeviceInfo() {
		return deviceDao.getOnlineDeviceInfo();
	}

	@Override
	public boolean isAuthorize(String card_num, String door, String deviceMac) {
		DeviceInfo deviceInfo = deviceDao.getDeviceInfoByMac(deviceMac);
		if (deviceInfo == null || deviceInfo.getDevice_num() == null) {// 根据设备mac地址获取设备编号失败
			return false;
		}
		Integer countAuthority = deviceDao.getAuthorityCount(card_num, door,
				deviceInfo.getDevice_num(),
				DateUtil.getDateString(new Date(), "yyyy-MM-dd"));
		if (countAuthority <= 0) {// 如果没有权限
			return false;
		}
		return true;
	}
}