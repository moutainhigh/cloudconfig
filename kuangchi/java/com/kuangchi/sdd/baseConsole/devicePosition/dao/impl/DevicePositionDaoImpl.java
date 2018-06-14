package com.kuangchi.sdd.baseConsole.devicePosition.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.devicePosition.dao.DevicePositionDao;
import com.kuangchi.sdd.baseConsole.devicePosition.model.DeviceDistributionPic;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-6-13 下午4:19:53
 * @功能描述: 设备坐标信息 - Dao实现类
 */
@Repository("devicePositionDaoImpl")
public class DevicePositionDaoImpl extends BaseDaoImpl<Object> implements DevicePositionDao {

	@Override
	public String getNameSpace() {
		return "common.DevicePosition";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public void setDevicePosition(DeviceInfo device) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_num", device.getDevice_num());
		map.put("x_position", device.getX_position());
		map.put("y_position", device.getY_position());
		map.put("pic_id", device.getDistribution_pic_id());
		this.getSqlMapClientTemplate().update("modifyDevicePosition", map);
	}

	@Override
	public List<DeviceDistributionPic> getDevicePositionInfo() {
		return this.getSqlMapClientTemplate().queryForList("getAllPic");
	}

	@Override
	public List<DeviceInfo> getDeviceByPic(String pic_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pic_id", pic_id);
		return this.getSqlMapClientTemplate().queryForList("getDeviceByPic", map);
	}

	@Override
	public void updateDeviceState(String device_num, int online_state) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_num", device_num);
		map.put("online_state", online_state);
		this.getSqlMapClientTemplate().update("updateDeviceOnlineState", map);
	}

	@Override
	public List<String> getDeviceByDeviceGroup(String device_group) {
		return this.getSqlMapClientTemplate().queryForList("getDeviceByDeviceGroup", device_group);
	}

	@Override
	public void removeDevicePosition(String device_num) {
		this.getSqlMapClientTemplate().update("removeDevicePosition", device_num);
	}

	@Override
	public DeviceInfo getDeviceByDeviceNum(String device_num) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_num", device_num);
		return (DeviceInfo) this.getSqlMapClientTemplate().queryForObject("getDeviceByDeviceNum", map);
	}

	
}
