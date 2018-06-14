package com.kuangchi.sdd.interfaceConsole.consume.consumeDeviceState.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.device.model.DeviceStateModel;
import com.kuangchi.sdd.interfaceConsole.consume.consumeDeviceState.dao.DeviceStateDao;

@Repository("deviceStateDaoImpl")
public class DeviceStateDaoImpl extends BaseDaoImpl<DeviceStateModel> implements
		DeviceStateDao {

	@Override
	public String getNameSpace() {
		return null;
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public boolean modifyOnlineState(String deviceNum, int onlineState) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("device_num", deviceNum);
		mapState.put("online_state", onlineState);
		int result = this.getSqlMapClientTemplate().update("modifyOnlineState",
				mapState);
		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean modifyBusyState(String deviceNum, int busyState) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("device_num", deviceNum);
		mapState.put("busy_state", busyState);
		int result = this.getSqlMapClientTemplate().update("modifyBusyState",
				mapState);
		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean initDevState() {
		int result = this.getSqlMapClientTemplate()
				.update("initDevState", null);
		if (result >= 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean modifyDevCommIp(String deviceNum, String commIpID) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("commIpID", commIpID);
		map.put("deviceNum", deviceNum == null ? "0" : deviceNum);
		int result = this.getSqlMapClientTemplate().update("modifyDevCommIp",
				map);
		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}

}
