package com.kuangchi.sdd.consumeConsole.device.dao.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.businessConsole.cron.model.Cron;
import com.kuangchi.sdd.consumeConsole.device.dao.IDeviceDao;
import com.kuangchi.sdd.consumeConsole.device.model.Device;
import com.kuangchi.sdd.consumeConsole.device.model.DeviceConsumeSet;
import com.kuangchi.sdd.consumeConsole.device.model.DeviceGood;
import com.kuangchi.sdd.consumeConsole.device.model.DeviceGroup;
import com.kuangchi.sdd.consumeConsole.device.model.PersonCardTask;
import com.kuangchi.sdd.consumeConsole.meal.model.MealModel;

/**
 * 设备信息维护Dao
 * 
 * @author minting.he
 * 
 */
@Repository("cDeviceDao")
public class CDeviceDaoImpl extends BaseDaoImpl<Device> implements IDeviceDao {

	public String getNameSpace() {
		return "comm.Device";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<Device> getDeviceByParam(Device device) {
		return getSqlMapClientTemplate().queryForList("getDeviceByParam",
				device);
	}

	@Override
	public Integer getDeviceByParamCount(Device device) {
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"getDeviceByParamCount", device);
	}

	@Override
	public Device selDeviceById(String device_num) {
		return (Device) getSqlMapClientTemplate().queryForObject(
				"selDeviceById", device_num);
	}

	@Override
	public Integer validNum(Device device) {
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"validDeviceNum", device);
	}

	@Override
	public boolean insertDevice(Device device) {
		return insert("insertDevice", device);
	}

	@Override
	public boolean updateDevice(Device device) {
		return update("updateDevice", device);
	}

	@Override
	public boolean deleteDevice(String device_num) {
		return delete("deleteDevice", device_num);
	}

	@Override
	public DeviceGood selByDeviceNum(String device_num) {
		return (DeviceGood) getSqlMapClientTemplate().queryForObject(
				"selByDeviceNum", device_num);
	}

	@Override
	public boolean insertDeviceGood(DeviceGood deviceGood) {
		return insert("insertDeviceGood", deviceGood);
	}

	@Override
	public boolean deleteDeviceGood(String device_num) {
		return update("deleteDeviceGood", device_num);
	}

	@Override
	public boolean updateDeviceGood(DeviceGood deviceGood) {
		return update("updateDeviceGood", deviceGood);
	}

	@Override
	public Integer typeNumIsExist(String device_type_num) {
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"typeNumIsExist", device_type_num);
	}

	@Override
	public List<String> selXFDeviceInGroup(String device_group_num) {
		return getSqlMapClientTemplate().queryForList("selXFDeviceInGroup",
				device_group_num);
	}

	@Override
	public List<Device> getAllXFDevice() {
		return getSqlMapClientTemplate().queryForList("getAllXFDevice");
	}

	@Override
	public List<DeviceGroup> getXFDeviceGroup() {
		return getSqlMapClientTemplate().queryForList("getXFDeviceGroup");
	}

	@Override
	public DeviceGroup getXFGroupInfoByNum(String group_num) {
		return (DeviceGroup) getSqlMapClientTemplate().queryForObject(
				"getXFGroupInfoByNum", group_num);
	}

	@Override
	public boolean updateXFDeviceGroup(DeviceGroup deviceGroup) {
		return update("updateXFDeviceGroup", deviceGroup);
	}

	@Override
	public boolean insertXFDeviceGroup(DeviceGroup deviceGroup) {
		return insert("insertXFDeviceGroup", deviceGroup);
	}

	@Override
	public boolean deleteXFDeviceGroup(String group_num) {
		return delete("deleteXFDeviceGroup", group_num);
	}

	@Override
	public boolean changeDeviceGroup(String device_group_num, String device_num) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_group_num", device_group_num);
		map.put("device_num", device_num);
		return update("changeDeviceGroup", map);
	}

	@Override
	public boolean changeParentGroup(String parent_group_num, String group_num) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parent_group_num", parent_group_num);
		map.put("group_num", group_num);
		return update("changeParentGroup", map);
	}

	@Override
	public List<Device> getDeviceByTree(Device device) {
		return getSqlMapClientTemplate()
				.queryForList("getDeviceByTree", device);
	}

	@Override
	public Integer getDeviceByTreeCount(Device device) {
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"getDeviceByTreeCount", device);
	}

	@Override
	public List<Map> getNameByDevice(Map map) {
		return getSqlMapClientTemplate().queryForList("getNameByDevice", map);
	}

	@Override
	public Integer getNameByDeviceCount(Map map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"getNameByDeviceCount", map);
	}

	@Override
	public List<BigDecimal> getBalanceByStaff(String staff_num) {
		return this.getSqlMapClientTemplate().queryForList("getBalanceByStaff",
				staff_num);
	}

	@Override
	public List<DeviceConsumeSet> getMealLimitTimesList(String device_num) {

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("device_num", device_num);
		return getSqlMapClientTemplate().queryForList("getMealLimitTimesList",
				map);
	}

	@Override
	public boolean delMealLimitTimes(String id, String device_num) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("device_num", device_num);
		return delete("delMealLimitTimes", map);
	}

	@Override
	public List<MealModel> getMealNum(String device_num) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("device_num", device_num);
		return getSqlMapClientTemplate().queryForList("getMealNumList", map);
	}

	@Override
	public Device selectDeviceByNum(String device_num) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("device_num", device_num);
		return (Device) getSqlMapClientTemplate().queryForObject(
				"selectDeviceByNum", map);
	}

	@Override
	public boolean addNewMealLimitTime(DeviceConsumeSet deviceConsumeSet) {
		return insert("addNewMealLimitTime", deviceConsumeSet);
	}

	@Override
	public DeviceConsumeSet getDeviceConsumeSetById(String id, String device_num) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("device_num", device_num);
		return (DeviceConsumeSet) getSqlMapClientTemplate().queryForObject(
				"getDeviceConsumeSetById", map);
	}

	@Override
	public boolean modifyMealLimitTime(DeviceConsumeSet deviceConsumeSet) {
		return update("modifyMealLimitTime", deviceConsumeSet);
	}

	@Override
	public int selectMealCount() {
		return queryCount("selectMealCount", null);
	}

	@Override
	public int selectDeviceMealCount(String device_num) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("device_num", device_num);
		return queryCount("selectDeviceMealCount", map);
	}

	@Override
	public boolean delAllMeal(String device_num) {
		return delete("delAllMeal", device_num);
	}

	@Override
	public Map getStaffByCard(String card_num) {
		return (Map) getSqlMapClientTemplate().queryForObject(
				"getStaffInfoByCard", card_num);
	}

	@Override
	public BigDecimal getAccountBalance(String device_num, String staff_num) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("device_num", device_num);
		map.put("staff_num", staff_num);
		return (BigDecimal) getSqlMapClientTemplate().queryForObject(
				"getAcBalance", map);
	}

	@Override
	public Integer isExistCardTask(String device_num, String card_num) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("device_num", device_num);
		map.put("card_num", card_num);
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"isExistCardTask", map);
	}

	@Override
	public boolean updateNameTask(PersonCardTask pcTask) {
		return update("updateNameTask", pcTask);
	}

	@Override
	public boolean insertNameTask(PersonCardTask pcTask) {
		return insert("insertNameTask", pcTask);
	}

	@Override
	public List<String> getAllCardNum() {
		return getSqlMapClientTemplate().queryForList("getAllCardNum");
	}

	/*
	 * @Override public List<PersonCardTask> getPersonCardTask(String machine) {
	 * return getSqlMapClientTemplate().queryForList("getPersonCardTask",
	 * machine); }
	 * 
	 * @Override public boolean deletePersonCardTask(String machine, String
	 * pack, String returnVal) { Map<String, String> map = new HashMap<String,
	 * String>(); map.put("returnVal", returnVal); map.put("machine", machine);
	 * map.put("pack", pack); return update("deletePersonCardTask", map); }
	 * 
	 * @Override public boolean updateTaskState(String idStr, String pack) {
	 * Map<String, Object> map = new HashMap<String, Object>(); map.put("pack",
	 * pack); map.put("idStr", idStr); return update("updateTaskState", map); }
	 * 
	 * @Override public Integer getMaxPack(String machine) { return (Integer)
	 * getSqlMapClientTemplate().queryForObject("getMaxPack", machine); }
	 * 
	 * @Override public Integer getTry_times(Integer maxPack, String machine) {
	 * Map<String, String> map = new HashMap<String, String>();
	 * map.put("maxPack", String.valueOf(maxPack)); map.put("machine", machine);
	 * return (Integer) getSqlMapClientTemplate().queryForObject(
	 * "getTry_times", map); }
	 * 
	 * @Override public List<PersonCardTask> getRunningTask(String machine) {
	 * return getSqlMapClientTemplate() .queryForList("getRunningTask",
	 * machine); }
	 * 
	 * @Override public boolean updateTaskTrytimes(String idStr, String
	 * tryTimes) { Map<String, Object> map = new HashMap<String, Object>();
	 * map.put("tryTimes", tryTimes); map.put("idStr", idStr); return
	 * update("updateTaskTrytimes", map); }
	 * 
	 * @Override public boolean insertToHistory(String machine) { return
	 * insert("insertToHistory", machine); }
	 * 
	 * @Override public boolean deletePCT(String machine) { return
	 * delete("deletePCT", machine); }
	 * 
	 * @Override public boolean markFail(String machine) { return
	 * update("markFail", machine); }
	 * 
	 * @Override public boolean insertToNameList(String card_num, String
	 * device_num, String create_time) { Map<String, String> map = new
	 * HashMap<String, String>(); map.put("card_num", card_num);
	 * map.put("device_num", device_num); map.put("create_time", create_time);
	 * return update("insertToNameList", map); }
	 * 
	 * @Override public List<PersonCardTask> getSuccNameList(String machine,
	 * String pack) { Map<String, Object> map = new HashMap<String, Object>();
	 * map.put("pack", pack); map.put("machine", machine); return
	 * getSqlMapClientTemplate().queryForList("getSuccNameList", map); }
	 * 
	 * @Override public Integer valName(String device_num, String card_num) {
	 * Map<String, Object> map = new HashMap<String, Object>();
	 * map.put("card_num", card_num); map.put("device_num", device_num); return
	 * (Integer) getSqlMapClientTemplate().queryForObject("valName", map); }
	 * 
	 * @Override public boolean updateName(String device_num, String card_num) {
	 * Map<String, String> map = new HashMap<String, String>();
	 * map.put("card_num", card_num); map.put("device_num", device_num); return
	 * update("updateName", map); }
	 */
	@Override
	public List<PersonCardTask> getSomeTask(String device_nums) {
		return getSqlMapClientTemplate().queryForList("getSomeTask",
				device_nums);
	}

	@Override
	public boolean deletePCTask(String id) {
		return delete("deletePCTask", id);
	}

	@Override
	public boolean insertToNameList(String card_num, String device_num,
			String create_time) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("card_num", card_num);
		map.put("device_num", device_num);
		map.put("create_time", create_time);
		return update("insertToNameList", map);
	}

	@Override
	public Integer valName(String device_num, String card_num) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("card_num", card_num);
		map.put("device_num", device_num);
		return (Integer) getSqlMapClientTemplate().queryForObject("valName",
				map);
	}

	@Override
	public boolean updateName(String device_num, String card_num) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("card_num", card_num);
		map.put("device_num", device_num);
		return update("updateName", map);
	}

	@Override
	public boolean insertToHistory(PersonCardTask personCardTask) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("task_id", personCardTask.getTask_id());
		map.put("device_num", personCardTask.getDevice_num());
		map.put("card_num", personCardTask.getCard_num());
		map.put("staff_id", personCardTask.getStaff_id());
		map.put("staff_no", personCardTask.getStaff_no());
		map.put("staff_num", personCardTask.getStaff_num());
		map.put("staff_name", personCardTask.getStaff_name());
		map.put("balance", personCardTask.getBalance());
		map.put("flag", personCardTask.getFlag());
		map.put("success_flag", personCardTask.getSuccess_flag());
		map.put("create_time", personCardTask.getCreate_time());
		map.put("try_times", personCardTask.getTry_times());
		map.put("trigger_flag", personCardTask.getTrigger_flag());
		return update("insertToHistory", map);
	}

	@Override
	public boolean setHistorySuccFlag(Integer succFlag, String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("succFlag", succFlag);
		map.put("id", id);
		return update("setHistorySuccFlag", map);
	}

	@Override
	public List<String> getOnlineDevice() {
		return getSqlMapClientTemplate().queryForList("getOnlineDevice", null);
	}

	@Override
	public boolean updateTryTime(PersonCardTask personCardTask) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("try_times", personCardTask.getTry_times());
		map.put("id", personCardTask.getId());
		return update("updateTryTime", map);
	}

	@Override
	public boolean updateRunState(String id, String state) {
		if (id != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("state", state);
			map.put("id", id);
			return update("updateRunState", map);
		} else {
			return update("updateRunState2", state);
		}
	}

	@Override
	public Integer compareIP(Cron cron) {
		return (Integer) getSqlMapClientTemplate().queryForObject("compareIP2",
				cron);
	}

	@Override
	public List<Map> getAllNameList(Map<String, Object> map) {
		return getSqlMapClientTemplate().queryForList("getAllNameList", map);
	}

	@Override
	public boolean delNameList(Map<String, Object> map) {
		return update("delNameList", map);
	}

	@Override
	public String getDeviceName(String device_num) {
		return (String) getSqlMapClientTemplate().queryForObject(
				"getDeviceName", device_num);
	}

	@Override
	public List<String> cardIfExistList(String card_num) {
		return getSqlMapClientTemplate().queryForList("cardIfExistList",
				card_num);
	}

	@Override
	public List<String> getAllDevNum() {
		return getSqlMapClientTemplate().queryForList("getAllDevNum", 1);
	}

	@Override
	public List<String> getNameListByDevice(String deviceNum) {
		return getSqlMapClientTemplate().queryForList("getNameListByDevice",
				deviceNum);
	}

	@Override
	public Map<String, String> getCommIPByDevNum(String deviceNum) {
		return (Map) getSqlMapClientTemplate().queryForObject(
				"getCommIPByDevNum", deviceNum);
	}

}
