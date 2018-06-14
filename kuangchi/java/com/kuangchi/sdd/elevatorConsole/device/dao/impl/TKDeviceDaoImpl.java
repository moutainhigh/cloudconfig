package com.kuangchi.sdd.elevatorConsole.device.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.device.model.DeviceGroup;
import com.kuangchi.sdd.elevatorConsole.device.dao.ITKDeviceDao;
import com.kuangchi.sdd.elevatorConsole.device.model.Device;
import com.kuangchi.sdd.elevatorConsole.device.model.Floor;
import com.kuangchi.sdd.elevatorConsole.device.model.Holiday;

/**
 * 设备信息维护Dao
 * 
 * @author minting.he
 * 
 */
@Repository("tkDeviceDao")
public class TKDeviceDaoImpl extends BaseDaoImpl<Device> implements
		ITKDeviceDao {

	public String getNameSpace() {
		return "common.TKDevice";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<Device> getTKDeviceListParam(Device device) {
		return getSqlMapClientTemplate().queryForList("getTKDeviceListParam",
				device);
	}

	@Override
	public Integer getTKDeviceListParamCount(Device device) {
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"getTKDeviceListParamCount", device);
	}

	@Override
	public List<DeviceGroup> getTKDeviceGroup() {
		return getSqlMapClientTemplate().queryForList("getTKDeviceGroup");
	}

	@Override
	public List<Device> getAllTKDevice() {
		return getSqlMapClientTemplate().queryForList("getAllTKDevice");
	}

	@Override
	public List<Device> getTKDeviceByTree(Device device) {
		return getSqlMapClientTemplate().queryForList("getTKDeviceByTree",
				device);
	}

	@Override
	public Integer getTKDeviceByTreeCount(Device device) {
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"getTKDeviceByTreeCount", device);
	}

	@Override
	public boolean changeTKDeviceGroup(String device_group_num,
			String device_num) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_group_num", device_group_num);
		map.put("device_num", device_num);
		return update("changeTKDeviceGroup", map);
	}

	@Override
	public boolean insertTKDevice(Device device) {
		return insert("insertTKDevice", device);
	}

	@Override
	public Integer ifExistTKDeviMac(Device device) {
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"ifExistTKDeviMac", device);
	}

	@Override
	public boolean insertDefaultFloor(Floor floor) {
		return insert("insertDefaultFloor", floor);
	}

	@Override
	public boolean deleteTKDevice(String device_num) {
		return update("deleteTKDevice", device_num);
	}

	@Override
	public boolean delDeviceFloor(String device_num) {
		return update("delDeviceFloor", device_num);
	}

	@Override
	public boolean delDeviceHoliday(String device_num) {
		return update("delDeviceHoliday", device_num);
	}

	@Override
	public Device getInfoByTKDeviceNum(String device_num) {
		return (Device) getSqlMapClientTemplate().queryForObject(
				"getInfoByTKDeviceNum", device_num);
	}

	@Override
	public Integer ifExistTKDeviName(Map<String, Object> map) {
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"ifExistTKDeviName", map);
	}

	@Override
	public boolean updateTKDevice(Device device) {
		return update("updateTKDevice", device);
	}

	@Override
	public boolean updateDeviceTime(String mac) {
		return update("updateDeviceTime", mac);
	}

	@Override
	public boolean updateTKDeviceState(Device device) {
		return update("updateTKDeviceState", device);
	}

	@Override
	public List<Floor> getDeviceFloor(String device_num) {
		return getSqlMapClientTemplate().queryForList("getDeviceFloor",
				device_num);
	}

	@Override
	public boolean updateDeviceFloor(Floor floor) {
		return update("updateDeviceFloor", floor);
	}

	@Override
	public boolean setFloorOpenArea(Floor floor) {
		return update("setFloorOpenArea", floor);
	}

	@Override
	public List<Floor> getFloorInfo(String device_num) {
		return getSqlMapClientTemplate().queryForList("getFloorInfo",
				device_num);
	}

	@Override
	public List<Holiday> getHolidayByDevice(Map<String, Object> map) {
		return getSqlMapClientTemplate()
				.queryForList("getHolidayByDevice", map);
	}

	@Override
	public Integer getHolidayByDeviceCount(Map<String, Object> map) {
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"getHolidayByDeviceCount", map);
	}

	@Override
	public List<Holiday> getHolisByDevi(String device_num) {
		return getSqlMapClientTemplate().queryForList("getHolisByDevi",
				device_num);
	}

	@Override
	public boolean insertHoliday(Holiday holiday) {
		return insert("insertHoliday", holiday);
	}

	@Override
	public boolean updateAllState(String online_state) {
		return update("updateAllState", online_state);
	}

	@Override
	public List<Device> getAllTKDeviceInfo() {
		return getSqlMapClientTemplate().queryForList("getAllTKDeviceInfo");
	}

	@Override
	public boolean delDeviceHoli(String device_num) {
		return delete("delDeviceHoli", device_num);
	}

	@Override
	public boolean updateHoliSend_State(Holiday holiday, int state) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("state", state);
		map.put("holiday_date", holiday.getHoliday_date());
		return update("updateHoliSend_State", map);
	}

	@Override
	public List<Holiday> getSendHoliday(String device_num) {
		return getSqlMapClientTemplate().queryForList("getSendHoliday",
				device_num);
	}
	
	@Override
	public boolean resetPosition(String mac){
		return update("resetPosition", mac);
	}
	
	@Override
	public List<Map<String, Object>> getFlagFloor(String device_num){
		return getSqlMapClientTemplate().queryForList("getFlagFloor", device_num);
	}
	
	@Override
	public boolean setIfEnabledFloor(Map<String, Object> map){
		return update("setIfEnabledFloor", map);
	}
	
	@Override
	public List<String> getIfEnabledFloor(Map<String, Object> map){
		return getSqlMapClientTemplate().queryForList("getIfEnabledFloor", map);
	}
	
	@Override
	public List<Floor> getFloorName(String device_num){
		return getSqlMapClientTemplate().queryForList("getNameFloor", device_num);
	}
	
	
}
