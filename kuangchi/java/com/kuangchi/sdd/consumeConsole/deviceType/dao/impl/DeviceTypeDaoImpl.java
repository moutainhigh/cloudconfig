package com.kuangchi.sdd.consumeConsole.deviceType.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.duty.model.Duty;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.deviceType.dao.IDeviceTypeDao;
import com.kuangchi.sdd.consumeConsole.deviceType.model.DeviceType;

@Repository("deviceTypeDao")
public class DeviceTypeDaoImpl extends BaseDaoImpl<DeviceType> implements IDeviceTypeDao {

	@Override
	public String getNameSpace() {
		return "common.DeviceType";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<DeviceType> getDeviceTypePage(DeviceType deviceType) {
		return getSqlMapClientTemplate().queryForList("getDeviceTypePage", deviceType);
	}

	@Override
	public Integer getDeviceTypeCounts(DeviceType deviceType) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getDeviceTypeCounts", deviceType);
	}

	@Override
	public DeviceType selDeviceTypeById(String id) {
		return (DeviceType) getSqlMapClientTemplate().queryForObject("selDeviceTypeById", id);
	}

	@Override
	public Integer validNum(DeviceType deviceType) {
		return (Integer) getSqlMapClientTemplate().queryForObject("validNum", deviceType);
	}

	@Override
	public boolean insertDeviceType(DeviceType deviceType) {
		return insert("insertDeviceType", deviceType);
	}

	@Override
	public boolean updateDeviceType(DeviceType deviceType) {
		return update("updateDeviceType", deviceType);
	}

	@Override
	public boolean deleteDeviceType(String ids) {
		return update("deleteDeviceType", ids);
	}

	@Override
	public List<DeviceType> getAllDeviceType() {
		return getSqlMapClientTemplate().queryForList("getAllDeviceType");
	}
	

}
