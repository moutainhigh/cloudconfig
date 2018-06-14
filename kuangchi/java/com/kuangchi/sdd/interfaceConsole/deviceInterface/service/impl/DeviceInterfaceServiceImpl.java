package com.kuangchi.sdd.interfaceConsole.deviceInterface.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.interfaceConsole.deviceInterface.dao.IDeviceInterfaceDao;
import com.kuangchi.sdd.interfaceConsole.deviceInterface.service.IDeviceInterfaceService;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-5-19 下午2:24:54
 * @功能描述: 对外接口-业务实现类
 */
@Transactional
@Service("deviceInterfaceServiceImpl")
public class DeviceInterfaceServiceImpl implements IDeviceInterfaceService {

	@Resource(name = "deviceInterfaceDaoImpl")
	private IDeviceInterfaceDao deviceInterfaceDao;

	@Override
	public DeviceInfo getDeviceByMac(String device_mac) {
		return deviceInterfaceDao.getDeviceByMac(device_mac);
	}
	

	

}
