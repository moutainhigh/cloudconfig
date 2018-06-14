package com.kuangchi.sdd.interfaceConsole.consume.consumeDeviceState.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.interfaceConsole.consume.consumeDeviceState.dao.DeviceStateDao;
import com.kuangchi.sdd.interfaceConsole.consume.consumeDeviceState.service.DeviceStateService;

@Service("deviceStateServiceImpl")
public class DeviceStateServiceImpl implements DeviceStateService {
	@Resource(name = "deviceStateDaoImpl")
	private DeviceStateDao deviceStateDao;

	@Override
	public boolean modifyOnlineState(String deviceNum, int onlineState) {
		boolean flag = deviceStateDao.modifyOnlineState(deviceNum, onlineState);
		;
		if (flag) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean modifyBusyState(String deviceNum, int busyState) {
		boolean flag = deviceStateDao.modifyBusyState(deviceNum, busyState);
		;
		if (flag) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean initDevState() {
		boolean flag = deviceStateDao.initDevState();
		if (flag) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean modifyDevCommIp(String deviceNum, String commIpID) {
		boolean flag = deviceStateDao.modifyDevCommIp(deviceNum, commIpID);
		if (flag) {
			return true;
		} else {
			return false;
		}
	}

}
