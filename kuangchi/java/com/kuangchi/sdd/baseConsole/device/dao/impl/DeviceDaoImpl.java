package com.kuangchi.sdd.baseConsole.device.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.baseConsole.device.dao.DeviceDao;
import com.kuangchi.sdd.baseConsole.device.model.DescriptionPicModel;
import com.kuangchi.sdd.baseConsole.device.model.DeviceAttriModel;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.device.model.DeviceStateModel;
import com.kuangchi.sdd.baseConsole.device.model.EquipmentBean;
import com.kuangchi.sdd.baseConsole.deviceGroup.model.DeviceGroupModel;

@Repository("deviceDao")
public class DeviceDaoImpl extends BaseDaoImpl<Object> implements DeviceDao {

	/* 查询设备状态 */
	@Override
	public List<DeviceStateModel> searchDeviceState(String deviceName,
			String deviceMac, Integer skip, Integer rows) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_name", deviceName);
		map.put("device_mac", deviceMac);
		map.put("skip", skip);
		map.put("rows", rows);
		return getSqlMapClientTemplate().queryForList("searchDeviceState", map);
	}

	/*
	 * 设备号精确查询名称
	 */
	public List<DeviceStateModel> exactDeviceName(String deviceNum,
			Integer skip, Integer rows) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deviceNum", deviceNum);
		map.put("skip", skip);
		map.put("rows", rows);
		return this.getSqlMapClientTemplate().queryForList("exactDeviceName",
				map);
	}

	@Override
	public Integer exactDeviceNameCount(String deviceNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deviceNum", deviceNum);
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"exactDeviceNameCount", map);
	}

	/* 设备组查询设备状态 */
	@Override
	public List<DeviceStateModel> searchDeviceByGroup(String groupNum,
			Integer skip, Integer rows) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupNum", groupNum);
		map.put("skip", skip);
		map.put("rows", rows);
		return this.getSqlMapClientTemplate().queryForList(
				"searchDeviceByGroup", map);
	}

	@Override
	public Integer searchDeviceByGroupCount(String groupNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupNum", groupNum);
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"searchDeviceByGroupCount", map);
	}

	@Override
	public void setDeviceAttribute(String deviceNum, String headerCardFlag,
			String oneOutControlFlag, String twoOutControlFlag,
			String oneLockControlFlag, String twoLockControlFlag,
			String threeLockControlFlag, String fourLockControlFlag,
			String delayOpenDoorTime, String fireFlag) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deviceNum", deviceNum);
		map.put("headerCardFlag", headerCardFlag);
		map.put("oneOutControlFlag", oneOutControlFlag);
		map.put("twoOutControlFlag", twoOutControlFlag);
		map.put("oneLockControlFlag", oneLockControlFlag);
		map.put("twoLockControlFlag", twoLockControlFlag);
		map.put("threeLockControlFlag", threeLockControlFlag);
		map.put("fourLockControlFlag", fourLockControlFlag);
		map.put("delayOpenDoorTime", delayOpenDoorTime);
		map.put("fireFlag", fireFlag);

		getSqlMapClientTemplate().update("setDeviceAttribute", map);

	}

	/* 查询设备状态数 */
	@Override
	public Integer searchDeviceStateCount(String deviceName, String deviceMac) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_name", deviceName);
		map.put("device_mac", deviceMac);
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"searchDeviceStateCount", map);
	}

	@Override
	public DeviceAttriModel getDeviceAttributeByDeviceNum(String deviceNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deviceNum", deviceNum);
		return (DeviceAttriModel) getSqlMapClientTemplate().queryForObject(
				"getDeviceAttributeByDeviceNum", map);
	}

	/* 根据设备编号获取设备状态 */
	@Override
	public DeviceStateModel getDeviceStateByDeviceNum(String deviceNum,
			String doorNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deviceNum", deviceNum);
		map.put("doorNum", doorNum);
		return (DeviceStateModel) getSqlMapClientTemplate().queryForObject(
				"getDeviceStateByDeviceNum", map);
	}

	/**
	 * 异步刷新读取设备状态
	 * 
	 * @return
	 */
	public List<DeviceStateModel> getDeviceStateByNum(String deviceNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deviceNum", deviceNum);
		return getSqlMapClientTemplate().queryForList(
				"getDeviceStateByDeviceNum", map);
	}

	@Override
	public List<DeviceAttriModel> searchDeviceAttribute(String deviceName,
			String deviceMac, Integer skip, Integer rows) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deviceName", deviceName);
		map.put("deviceMac", deviceMac);
		map.put("skip", skip);
		map.put("rows", rows);
		return getSqlMapClientTemplate().queryForList("searchDeviceAttribute",
				map);
	}

	@Override
	public Integer searchDeviceAttributeCount(String deviceName,
			String deviceMac) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deviceName", deviceName);
		map.put("deviceMac", deviceMac);
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"searchDeviceAttributeCount", map);
	}

	@Override
	public String getNameSpace() {
		// TODO Auto-generated method stub
		return "common.Device";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateDeviceState(String deviceNum, String doorNum,
			String lockState, String doorState, String keyState,
			String skidState, String fireState) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deviceNum", deviceNum);
		map.put("doorNum", doorNum);
		map.put("lockState", lockState);
		map.put("doorState", doorState);
		map.put("keyState", keyState);
		map.put("skidState", skidState);
		map.put("fireState", fireState);
		getSqlMapClientTemplate().update("updateDeviceState", map);

	}

	@Override
	public String getDeviceNumByMac(String mac) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mac", mac);
		return (String) getSqlMapClientTemplate().queryForObject(
				"getDeviceNumByMac", map);
	}

	@Override
	public void addDeviceState(String deviceNum, String doorNum,
			String lockState, String doorState, String keyState,
			String skidState, String fireState) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deviceNum", deviceNum);
		map.put("doorNum", doorNum);
		map.put("lockState", lockState);
		map.put("doorState", doorState);
		map.put("keyState", keyState);
		map.put("skidState", skidState);
		map.put("fireState", fireState);
		getSqlMapClientTemplate().insert("addDeviceState", map);
	}

	@Override
	public List<DeviceInfo> getDeviceInfo(String device_name,
			String device_mac, Integer page, Integer size) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_mac", device_mac);
		map.put("device_name", device_name);
		map.put("page", page);
		map.put("size", size);
		return getSqlMapClientTemplate().queryForList("getDeviceInfo", map);
	}

	@Override
	public Integer getDeviceInfoCount(String device_name, String device_mac) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_mac", device_mac);
		map.put("device_name", device_name);
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"getDeviceInfoCount", map);
	}

	@Override
	public JsonResult modifyDeviceInfo(DeviceInfo deviceinfo) {
		// TODO Auto-generated method stub
		JsonResult result = new JsonResult();
		int count = (Integer) getSqlMapClientTemplate().update(
				"modifyDeviceInfo", deviceinfo);
		if (count == 0) {
			result.setSuccess(false);
		} else {
			result.setSuccess(true);
		}

		return result;
	}

	// 删除设备
	@Transactional
	@Override
	public JsonResult deleteDeviceInfo(String device_num) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_num", device_num);
		map.put("validity_flag", "1");
		JsonResult result = new JsonResult();
		int count_info = (Integer) getSqlMapClientTemplate().update(
				"deleteDeviceInfo", map);
		Object obj_attr = getSqlMapClientTemplate().delete("deleteDeviceAttr",
				map);
		Object obj_state = getSqlMapClientTemplate().delete(
				"deleteDeviceState", map);
		Object obj_door = getSqlMapClientTemplate().delete("deleteDoorInfo",
				map);
		Object obj_auth = getSqlMapClientTemplate().delete(
				"deleteAuthorityInfo", map);
		Object times = getSqlMapClientTemplate().delete("deleteDeviceTimes",
				device_num);
		Object timesGroup = getSqlMapClientTemplate().delete(
				"deleteDeviceTimesGroup", device_num);
		if (count_info == 0 || obj_attr == null || obj_state == null
				|| obj_door == null || obj_auth == null || times == null
				|| timesGroup == null) {
			throw new RuntimeException();
		}
		result.setSuccess(true);

		return result;
	}

	@Transactional
	@Override
	public JsonResult initializeDeviceInfo(String device_num) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_num", device_num);
		JsonResult result = new JsonResult();
		int count_info = (Integer) getSqlMapClientTemplate().update(
				"initializeDeviceInfo", map);
		int count_state = (Integer) getSqlMapClientTemplate().update(
				"initializeDeviceState", map);
		int count_attr = (Integer) getSqlMapClientTemplate().update(
				"initializeDeviceAttr", map);
		if (count_info == 0 || count_state == 0 || count_attr == 0) {
			throw new RuntimeException();
		}
		result.setSuccess(true);

		return result;
	}

	@Transactional
	@Override
	public Integer addDeviceInfo(EquipmentBean equipmentbean) {
		// TODO Auto-generated method stub
		Object obj_state = null;
		Object obj_attr = null;
		int count = (Integer) getSqlMapClientTemplate().queryForObject(
				"getDeviceCount", equipmentbean);// 判断是否已有该设备
		// 判断是否已有被删除的设备（主要用于搜索设备数量的累加）
		int counted = (Integer) getSqlMapClientTemplate().queryForObject(
				"DeviceCounted", equipmentbean);
		if (count == 1) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("mac", equipmentbean.getMac());
			// 根据mac地址去查询设备编号
			String device_num = (String) getSqlMapClientTemplate()
					.queryForObject("getDeviceNumByMac", map);
			Integer obj = (Integer) getSqlMapClientTemplate().update(
					"searchUpdateDeviceInfo", equipmentbean);
			equipmentbean.setDevicenum(device_num);
			String device_num_state = (String) getSqlMapClientTemplate()
					.queryForObject("searchDeviceStateByDev", device_num);
			String device_num_attr = (String) getSqlMapClientTemplate()
					.queryForObject("searchDeviceAttrByDev", device_num);
			List times = getSqlMapClientTemplate().queryForList("getmjTimes",
					device_num);// 查询设备时段表
			List timesGroup = getSqlMapClientTemplate().queryForList(
					"getTimeGroup", device_num);// 查询设备时段组表
			if (times.size() == 0) {
				getSqlMapClientTemplate().insert("addmjTimes", device_num);
			}
			if (timesGroup.size() == 0) {
				Map<String, Object> maps = new HashMap<String, Object>();
				for (int i = 0; i < 16; i++) {
					for (int j = 0; j < 8; j++) {
						maps.put("group_num", i);
						maps.put("group_name", "用户时段组" + i);
						maps.put("device_num", device_num);
						getSqlMapClientTemplate().insert("addTimeGroup", maps);
					}
				}
			}

			if (device_num_state == null) {
				if (equipmentbean.getDevicetype().equals("4")) {
					for (int i = 1; i < 5; i++) {
						equipmentbean.setDoornum(i);
						obj_state = getSqlMapClientTemplate().insert(
								"addDevice_State", equipmentbean);
					}
				} else if (equipmentbean.getDevicetype().equals("2")) {
					for (int i = 1; i < 3; i++) {
						equipmentbean.setDoornum(i);
						obj_state = getSqlMapClientTemplate().insert(
								"addDevice_State", equipmentbean);
					}
				} else {
					equipmentbean.setDoornum(1);
					obj_state = getSqlMapClientTemplate().insert(
							"addDevice_State", equipmentbean);
				}
			}
			if (device_num_attr == null) {
				obj_attr = getSqlMapClientTemplate().insert("addDeviceAttr",
						equipmentbean);
			}
			if (obj_state != null || obj_attr != null) {
				throw new RuntimeException();
			}
			// return obj;
			if (counted == 1) {
				return 1;
			}
			return 0;
		}
		Object obj_info = getSqlMapClientTemplate().insert("addDeviceInfo",
				equipmentbean);
		if (equipmentbean.getDevicetype().equals("4")) {
			for (int i = 1; i < 5; i++) {
				equipmentbean.setDoornum(i);
				obj_state = getSqlMapClientTemplate().insert("addDevice_State",
						equipmentbean);
			}
		} else if (equipmentbean.getDevicetype().equals("2")) {
			for (int i = 1; i < 3; i++) {
				equipmentbean.setDoornum(i);
				obj_state = getSqlMapClientTemplate().insert("addDevice_State",
						equipmentbean);
			}
		} else {
			equipmentbean.setDoornum(1);
			obj_state = getSqlMapClientTemplate().insert("addDevice_State",
					equipmentbean);
		}
		List times = getSqlMapClientTemplate().queryForList("getmjTimes",
				equipmentbean.getDevicenum());// 查询设备时段表
		List timesGroup = getSqlMapClientTemplate().queryForList(
				"getTimeGroup", equipmentbean.getDevicenum());// 查询设备时段组表
		if (times.size() == 0) {
			getSqlMapClientTemplate().insert("addmjTimes",
					equipmentbean.getDevicenum());
		}
		if (timesGroup.size() == 0) {
			Map<String, Object> maps = new HashMap<String, Object>();
			for (int i = 0; i < 16; i++) {
				for (int j = 0; j < 8; j++) {
					maps.put("group_num", i);
					maps.put("group_name", "用户时段组" + i);
					maps.put("device_num", equipmentbean.getDevicenum());
					getSqlMapClientTemplate().insert("addTimeGroup", maps);
				}
			}
		}
		obj_attr = getSqlMapClientTemplate().insert("addDeviceAttr",
				equipmentbean);
		if (obj_info != null || obj_state != null || obj_attr != null) {
			throw new RuntimeException();
		}
		return 1;
	}

	// 查询设备类型
	@Override
	public String getDeviceType(String device_num) {
		return (String) getSqlMapClientTemplate().queryForObject(
				"getDeviceType", device_num);
	}

	@Override
	public Boolean insertDeviceTimeGroup(Map<String, Object> map) {
		return (Boolean) getSqlMapClientTemplate().insert(
				"insertDeviceTimeGroup", map);

	}

	@Override
	public Integer deleteDeviceTimeGroup(String device_num) {
		return (Integer) getSqlMapClientTemplate().delete(
				"deleteDeviceTimeGroup", device_num);
	}

	@Override
	public Map getMacByDeviceNum(String deviceNum) {
		return (Map) getSqlMapClientTemplate().queryForObject(
				"getMacByDeviceNum", deviceNum);
	}

	// 删除门卡信息表
	@Override
	public Integer deleteDoorPeopleauthorityService(String device_num) {
		return (Integer) getSqlMapClientTemplate().update(
				"deleteDoorPeopleauthority", device_num);
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-6-8 下午15:32:05
	 * @功能描述: 查询设备分布背景图(分页)
	 */
	public List<DescriptionPicModel> getDescriptionPictures(String flag,
			String description, String device_group_num, String page,
			String size) {
		Map searchMap = new HashMap();
		searchMap.put("flag", flag);
		searchMap.put("description", description);
		searchMap.put("device_group_num", device_group_num);
		searchMap.put("page",
				(Integer.valueOf(page) - 1) * Integer.valueOf(size));
		searchMap.put("size", Integer.valueOf(size));
		return getSqlMapClientTemplate().queryForList("getDescriptionPictures",
				searchMap);
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-6-8 下午15:32:05
	 * @功能描述: 查询设备分布背景图-条目数(分页)
	 */
	public Integer getDescriptionPicturesCount(String flag, String description,
			String device_group_num) {
		Map searchMap = new HashMap();
		searchMap.put("flag", flag);
		searchMap.put("description", description);
		searchMap.put("device_group_num", device_group_num);
		return (Integer) find("getDescriptionPicturesCount", searchMap);
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-6-12 上午10:32:05
	 * @功能描述: 删除设备分布背景图(伪)
	 */
	public boolean deleteDescriptionPicturesByIds(String ids) {
		Map searchMap = new HashMap();
		searchMap.put("ids", ids);
		return delete("deleteDescriptionPicturesByIds", searchMap);
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-6-8 下午15:32:05
	 * @功能描述: 查询设备分布背景图(不分页)
	 */
	public List<DescriptionPicModel> getDescriptionPicturesNoPage(int id,
			String description, String device_group_num) {
		Map searchMap = new HashMap();
		searchMap.put("description", description);
		searchMap.put("device_group_num", device_group_num);
		searchMap.put("description", description);
		searchMap.put("id", id);
		return getSqlMapClientTemplate().queryForList(
				"getDescriptionPicturesNoPage", searchMap);
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-6-12 上午10:32:05
	 * @功能描述: 新增设备分布背景图
	 */
	public boolean addDescriptionPicture(String flag, String description,
			String pic_path, String device_group_num) {
		Map searchMap = new HashMap();
		searchMap.put("flag", flag);
		searchMap.put("description", description);
		searchMap.put("pic_path", pic_path);
		searchMap.put("device_group_num", device_group_num);
		return insert("addDescriptionPicture", searchMap);
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-6-12 上午10:32:05
	 * @功能描述: 修改设备分布背景图
	 */
	public boolean updateDescriptionPicture(String description,
			String pic_path, String device_group_num, int id) {
		Map searchMap = new HashMap();
		searchMap.put("description", description);
		searchMap.put("pic_path", pic_path);
		searchMap.put("device_group_num", device_group_num);
		searchMap.put("id", id);
		return update("updateDescriptionPicture", searchMap);
	}

	@Override
	public DeviceInfo getDeviceInfoByNum(String deviceNum) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("deviceNum", deviceNum);
		return (DeviceInfo) getSqlMapClientTemplate().queryForObject(
				"getDeviceInfoByNum", map);
	}

	@Override
	public DeviceInfo getDeviceInfoByMac(String mac) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("mac", mac);
		return (DeviceInfo) getSqlMapClientTemplate().queryForObject(
				"getDeviceInfoByMac", map);
	}

	@Override
	public void updateDealState(String deviceNum, String eventDms) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("deviceNum", deviceNum);
		map.put("eventDms", eventDms);
		getSqlMapClientTemplate().update("updateDealState", map);
	}

	@Override
	public String getMacByDeviNum(String device_num) {
		return (String) this.getSqlMapClientTemplate().queryForObject(
				"getMacByDeviNum", device_num);
	}

	// 按筛选条件导出设备信息
	@Override
	public List<DeviceInfo> exportDevice(String device_name, String device_mac) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_mac", device_mac);
		map.put("device_name", device_name);
		return getSqlMapClientTemplate().queryForList("exportDevice", map);
	}

	@Override
	public void updateDeviceRecordByMac(Map map) {
		getSqlMapClientTemplate().update("updateDeviceRecordByMac", map);

	}

	@Override
	public String getDoorByDeviNum(String device_mac) {
		return (String) this.getSqlMapClientTemplate().queryForObject(
				"getDoorByDeviNum", device_mac);
	}

	@Override
	public Boolean addDoorinfo(Map<String, Object> mapp) {
		return (Boolean) getSqlMapClientTemplate().insert("addDoorinfo", mapp);
	}

	@Override
	public String getDeviceByDeviNum(String device_num) {
		return (String) this.getSqlMapClientTemplate().queryForObject(
				"getDeviceByDeviNum", device_num);
	}

	@Override
	public String getDeleteDoorByDeviNum(String device_num) {
		return (String) this.getSqlMapClientTemplate().queryForObject(
				"getDeleteDoorByDeviNum", device_num);
	}

	@Override
	public Integer updateDeleteDoorByNum(String device_num) {
		return (Integer) getSqlMapClientTemplate().update(
				"updateDeleteDoorByNum", device_num);
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-7-15 下午15:32:05
	 * @功能描述: 查询设备组
	 */
	public List<DeviceGroupModel> getDiviceGroupToPic(String flag) {
		return (List<DeviceGroupModel>) getSqlMapClientTemplate().queryForList(
				"getDiviceGroupToPic", flag);
	}

	@Override
	public List<String> getAllTimeGroups() {
		return getSqlMapClientTemplate().queryForList("getAllTimeGroups");
	}

	@Override
	public List<String> getAllDoorNum() {
		return getSqlMapClientTemplate().queryForList("getAllDoorNum");
	}

	@Override
	public List<String> getAllDeviceNum() {
		return getSqlMapClientTemplate().queryForList("getAllDeviceNum");
	}

	@Override
	public String getDeviceMacByDeviceNum(String deviceMac) {
		return (String) getSqlMapClientTemplate().queryForObject(
				"getDeviceMacByDeviceNum", deviceMac);
	}

	@Override
	public boolean setOpenFireConsole(Map map) {
		return update("setOpenFireConsole", map);
	}

	@Override
	public DeviceInfo getInfoByMac(String device_mac) {
		return (DeviceInfo) getSqlMapClientTemplate().queryForObject(
				"getInfoByMac", device_mac);
	}

	@Override
	public List<Map<String, Object>> getNowDeviceState() {
		try {
			return getSqlMapClientTemplate().queryForList("getNowDeviceState");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean updateEveryDeviState(Map map) {
		try {
			return this.update("updateEveryDeviState", map);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Integer ifRelatedPic(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"ifRelatedPic", map);
	}

	@Override
	public DeviceInfo getDeviceIpByDeviceMac(String mac) {
		return (DeviceInfo) getSqlMapClientTemplate().queryForObject(
				"getDeviceIpByDeviceMac", mac);
	}

	@Override
	public boolean updateDeviceVersion(Map map) {
		return update("updateDeviceVersion", map);
	}

	@Override
	public List<Map<String, Object>> getAllDeviceState(List<String> list) {
		return getSqlMapClientTemplate()
				.queryForList("getAllDeviceState", list);
	}

	@Override
	public boolean resetPosi(String device_mac) {
		return update("resetPosi", device_mac);
	}

	@Override
	public List<Map<String, Object>> getAllDeviceAuthorityInfo(String device_num) {
		return getSqlMapClientTemplate().queryForList(
				"getAllDeviceAuthorityInfo", device_num);
	}

	@Override
	public Boolean deleteAuthorityInfoByNum(String device_num) {
		return delete("deleteAuthorityInfoByNum", device_num);
	}

	@Override
	public Boolean insertHistoryAuthorityInfo(Map map) {
		return insert("insertHistoryAuthorityInfo", map);
	}

	@Override
	public String getTypeByMac(String mac) {
		return (String) getSqlMapClientTemplate().queryForObject(
				"getTypeByMac", mac);
	}

	@Override
	public List<DeviceInfo> getOnlineDeviceInfo() {
		return getSqlMapClientTemplate().queryForList("getOnlineDeviceInfo");
	}

	@Override
	public List<String> selectBmmc(String staff_no) {
		Map map = new HashMap();
		map.put("staff_no", staff_no);
		return this.getSqlMapClientTemplate().queryForList("selectBmmc", map);
	}

	@Override
	public Integer getAuthorityCount(String card_num, String door,
			String device, String date) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("card_num", card_num);
		map.put("door", door);
		map.put("device", device);
		map.put("date", date);
		return (Integer) find("getAuthorityCount", map);
	}

}